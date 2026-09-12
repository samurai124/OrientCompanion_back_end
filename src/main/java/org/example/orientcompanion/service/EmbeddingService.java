package org.example.orientcompanion.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "embeddings")
public class EmbeddingService {

    private final WebClient webClient;

    @Value("${gemini.api.url:${llm.embedding-api-url:}}")
    private String apiUrl;

    @Value("${gemini.api.key:${llm.api-key:}}")
    private String apiKey;

    @Value("${gemini.embedding.model:${llm.embedding-model:text-embedding-004}}")
    private String model;

    @Value("${llm.timeout-ms:8000}")
    private long timeoutMs;

    public EmbeddingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Cacheable(key = "#text", condition = "#text != null && !#text.isBlank()", unless = "#result == null")
    public float[] embed(String text) {
        String endpoint = resolveEmbeddingUrl();
        if (endpoint == null || text == null || text.isBlank()) {
            return null;
        }

        try {
            EmbeddingResponse response = webClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(new EmbeddingRequest(model, text))
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

    private String resolveEmbeddingUrl() {
        if (apiUrl == null || apiUrl.isBlank()) {
            return null;
        }
        String cleanUrl = apiUrl.trim();
        if (cleanUrl.endsWith("/embeddings")) {
            return cleanUrl;
        }
        return cleanUrl.endsWith("/") ? cleanUrl + "embeddings" : cleanUrl + "/embeddings";
    }

    private record EmbeddingRequest(String model, String input) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingResponse(List<EmbeddingData> data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingData(float[] embedding) {}
}
