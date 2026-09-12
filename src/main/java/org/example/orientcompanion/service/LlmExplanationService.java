package org.example.orientcompanion.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "llm_explanations")
public class LlmExplanationService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.model}")
    private String model;

    @Value("${llm.timeout-ms:8000}")
    private long timeoutMs;

    public LlmExplanationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Cacheable(key = "#student.id + ':' + #field.id + ':' + T(Math).round(#score)")
    public String generateExplanation(Student student, Field field, double score) {
        if (apiUrl == null || apiUrl.isBlank()) {
            log.warn("LLM non configuré (llm.api-url vide) — utilisation du mode dégradé");
            return fallbackExplanation(field, score);
        }

        try {
            return callLlm(buildPrompt(student, field, score));
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

        String endpoint = resolveChatUrl();
        ChatResponse response = webClient.post()
                .uri(endpoint)
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

    private String fallbackExplanation(Field field, double score) {
        String niveau = score >= 75 ? "une très bonne correspondance"
                : score >= 50 ? "une correspondance modérée"
                  : "une correspondance limitée";

        return "La filière \"%s\" présente %s avec votre profil, avec un score de %.1f/100 "
                .formatted(field.getName(), niveau, score)
                + "basé sur vos intérêts, votre profil de personnalité et vos résultats académiques déclarés. "
                + "Nous vous recommandons d'échanger avec un conseiller pour approfondir cette piste.";
    }

    private String resolveChatUrl() {
        if (apiUrl == null || apiUrl.isBlank()) {
            return null;
        }
        String cleanUrl = apiUrl.trim();
        if (cleanUrl.endsWith("/chat/completions")) {
            return cleanUrl;
        }
        return cleanUrl.endsWith("/") ? cleanUrl + "chat/completions" : cleanUrl + "/chat/completions";
    }

    private record ChatRequest(String model, List<ChatMessage> messages) {}

    private record ChatMessage(String role, String content) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatResponse(List<Choice> choices) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Choice(ChatMessage message) {}
}