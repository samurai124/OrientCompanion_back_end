package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orientcompanion.util.EmbeddingCodec;
import org.example.orientcompanion.util.VectorUtils;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.repository.FieldRepository;
import org.example.orientcompanion.entity.Recommendation;
import org.example.orientcompanion.repository.RecommendationRepository;
import org.example.orientcompanion.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Orchestre le moteur de recommandation :
 * 1. Pour chaque filière du catalogue, calcule le score structuré (ScoringService).
 * 2. Calcule la similarité vectorielle si les deux embeddings sont disponibles.
 * 3. Fusionne les deux scores selon les poids configurés (mode dégradé si
 *    les embeddings sont absents : le score structuré compte alors à 100%).
 * 4. Génère l'explication via LlmExplanationService (avec son propre fallback).
 * 5. Remplace les anciennes recommandations de l'étudiant par les nouvelles.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final FieldRepository fieldRepository;
    private final RecommendationRepository recommendationRepository;
    private final ScoringService scoringService;
    private final LlmExplanationService llmExplanationService;

    @Value("${scoring.fusion.structured:0.60}")
    private double structuredWeight;

    @Value("${scoring.fusion.vector:0.40}")
    private double vectorWeight;

    @Transactional
    public List<Recommendation> generateRecommendations(Student student) {
        List<Field> fields = fieldRepository.findAll();

        if (fields.isEmpty()) {
            log.warn("Aucune filière dans le catalogue — impossible de générer des recommandations");
            return List.of();
        }

        recommendationRepository.deleteByStudentId(student.getId());

        List<Recommendation> recommendations = fields.stream()
                .map(field -> buildRecommendation(student, field))
                .sorted(Comparator.comparingDouble(Recommendation::getScore).reversed())
                .toList();

        return recommendationRepository.saveAll(recommendations);
    }

    private Recommendation buildRecommendation(Student student, Field field) {
        double finalScore = computeFusedScore(student, field);
        String explanation = llmExplanationService.generateExplanation(student, field, finalScore);

        return Recommendation.builder()
                .student(student)
                .field(field)
                .score(finalScore)
                .explanation(explanation)
                .build();
    }

    private double computeFusedScore(Student student, Field field) {
        double structuredScore = scoringService.calculateStructuredScore(student, field);

        float[] studentEmbedding = EmbeddingCodec.fromJson(student.getProfileEmbedding());
        float[] fieldEmbedding = EmbeddingCodec.fromJson(field.getFieldEmbedding());

        if (studentEmbedding == null || fieldEmbedding == null) {
            // Mode dégradé : pas d'embeddings disponibles, le score structuré compte seul
            return structuredScore;
        }

        double cosineSimilarity = VectorUtils.cosineSimilarity(studentEmbedding, fieldEmbedding);
        double vectorScore = ((cosineSimilarity + 1) / 2) * 100; // normalise [-1,1] -> [0,100]

        return (structuredScore * structuredWeight) + (vectorScore * vectorWeight);
    }
}