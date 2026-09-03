package org.example.orientcompanion.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orientcompanion.exception.BusinessException;

public final class EmbeddingCodec {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private EmbeddingCodec() {
    }

    public static String toJson(float[] embedding) {
        try {
            return MAPPER.writeValueAsString(embedding);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Impossible de sérialiser l'embedding");
        }
    }

    public static float[] fromJson(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return MAPPER.readValue(json, float[].class);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Impossible de désérialiser l'embedding");
        }
    }
}