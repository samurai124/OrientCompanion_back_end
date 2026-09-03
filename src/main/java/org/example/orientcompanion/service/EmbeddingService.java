package org.example.orientcompanion.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Génère l'embedding vectoriel d'un texte via une API compatible OpenAI
 * (endpoint /embeddings). Retourne null en cas d'échec plutôt que de lever
 * une exception — le moteur de recommandation doit pouvoir fonctionner en
 * mode dégradé (scoring structuré seul) si ce service est indisponible.
 */
@Slf4j
@Service
public class EmbeddingService {

    private final WebClient webClient;

    @Value("${llm.embedding-api-url:}")
    private String apiUrl;

    @Value("${llm.api-key:}")
    private String apiKey;

    @Value("${llm.embedding-model:text-embedding-3-small}")
    private String model;

    @Value("${llm.timeout-ms:8000}")
    private long timeoutMs;

    public EmbeddingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * @return le vecteur d'embedding, ou null si le service est indisponible
     *         ou non configuré (le moteur de recommandation doit gérer ce cas).
     */
    public float[] embed(String text) {
        if (apiUrl == null || apiUrl.isBlank() || text == null || text.isBlank()) {
            return null;
        }

        try {
            EmbeddingRequest request = new EmbeddingRequest(model, text);

            EmbeddingResponse response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(EmbeddingResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response == null || response.data() == null || response.data().isEmpty()) {
                throw new IllegalStateException("Réponse d'embedding vide");
            }

            return response.data().get(0).embedding();

        } catch (Exception e) {
            log.error("Échec de la génération d'embedding : {}", e.getMessage());
            return null;
        }
    }

    private record EmbeddingRequest(String model, String input) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingResponse(List<EmbeddingData> data) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingData(float[] embedding) {
    }
}