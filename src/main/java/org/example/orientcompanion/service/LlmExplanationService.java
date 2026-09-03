package org.example.orientcompanion.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Génère l'explication en langage naturel d'une recommandation via un appel LLM.
 *
 * Suppose une API compatible "chat completions" (format OpenAI-like, supporté
 * par la plupart des fournisseurs à coût maîtrisé : Groq, Mistral, OpenAI...).
 *
 * En cas d'échec (timeout, erreur réseau, réponse invalide), bascule sur une
 * explication générée à partir du seul scoring structuré (mode dégradé,
 * cf. cahier des charges section 8 - Fiabilité).
 */
@Slf4j
@Service
public class LlmExplanationService {

    private final WebClient webClient;

    @Value("${llm.api-url}")
    private String apiUrl;

    @Value("${llm.api-key}")
    private String apiKey;

    @Value("${llm.timeout-ms:8000}")
    private long timeoutMs;

    @Value("${llm.model:gpt-4o-mini}")
    private String model;

    public LlmExplanationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateExplanation(Student student, Field field, double score) {
        if (apiUrl == null || apiUrl.isBlank()) {
            log.warn("LLM non configuré (llm.api-url vide) — utilisation du mode dégradé");
            return fallbackExplanation(field, score);
        }

        try {
            String prompt = buildPrompt(student, field, score);
            return callLlm(prompt);
        } catch (Exception e) {
            log.error("Échec de l'appel LLM, bascule en mode dégradé :", e);
            return fallbackExplanation(field, score);
        }
    }

    private String callLlm(String prompt) {
        ChatRequest request = new ChatRequest(
                model,
                List.of(
                        new ChatMessage("system", "Tu es un conseiller d'orientation scolaire. "
                                + "Explique en 2-3 phrases claires et bienveillantes pourquoi une filière "
                                + "correspond au profil d'un étudiant, sans jamais présenter cela comme une "
                                + "décision définitive."),
                        new ChatMessage("user", prompt)
                )
        );

        ChatResponse response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .timeout(Duration.ofMillis(timeoutMs))
                .block();

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new IllegalStateException("Réponse LLM vide ou invalide");
        }

        return response.choices().get(0).message().content().trim();
    }

    private String buildPrompt(Student student, Field field, double score) {
        return """
                Étudiant intéressé par : %s
                Filière évaluée : %s
                Description de la filière : %s
                Score de compatibilité calculé : %.1f/100

                Explique pourquoi cette filière correspond (ou pas totalement) au profil de l'étudiant.
                """.formatted(
                student.getInterestsJson(),
                field.getName(),
                field.getDescription(),
                score
        );
    }

    /**
     * Mode dégradé : explication générée uniquement à partir du score structuré,
     * sans appel externe. Toujours disponible, jamais d'échec possible.
     */
    private String fallbackExplanation(Field field, double score) {
        String niveau;
        if (score >= 75) {
            niveau = "une très bonne correspondance";
        } else if (score >= 50) {
            niveau = "une correspondance modérée";
        } else {
            niveau = "une correspondance limitée";
        }

        return "La filière \"%s\" présente %s avec votre profil, avec un score de %.1f/100 "
                .formatted(field.getName(), niveau, score)
                + "basé sur vos intérêts, votre profil de personnalité et vos résultats académiques déclarés. "
                + "Nous vous recommandons d'échanger avec un conseiller pour approfondir cette piste.";
    }

    // --- DTOs internes pour l'appel API (format chat completions) ---

    private record ChatRequest(String model, List<ChatMessage> messages) {
    }

    private record ChatMessage(String role, String content) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatResponse(List<Choice> choices) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Choice(ChatMessage message) {
    }
}