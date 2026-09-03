package org.example.orientcompanion.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orientcompanion.exception.BusinessException;

import java.util.Collections;
import java.util.Map;

public final class JsonMapCodec {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Double>> MAP_TYPE = new TypeReference<>() {
    };

    private JsonMapCodec() {
    }

    public static String toJson(Map<String, Double> map) {
        try {
            return MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Impossible de sérialiser les données JSON");
        }
    }

    public static Map<String, Double> fromJson(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return MAPPER.readValue(json, MAP_TYPE);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Impossible de désérialiser les données JSON");
        }
    }
}