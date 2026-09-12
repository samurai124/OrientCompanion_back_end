package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orientcompanion.dto.RecommendationResponse;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.Recommendation;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.mapper.RecommendationMapper;
import org.example.orientcompanion.repository.FieldRepository;
import org.example.orientcompanion.repository.RecommendationRepository;
import org.example.orientcompanion.repository.StudentRepository;
import org.example.orientcompanion.util.EmbeddingCodec;
import org.example.orientcompanion.util.VectorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "recommendations")
public class RecommendationService {

    private final FieldRepository fieldRepository;
    private final RecommendationRepository recommendationRepository;
    private final StudentRepository studentRepository;
    private final ScoringService scoringService;
    private final LlmExplanationService llmExplanationService;
    private final RecommendationMapper recommendationMapper;
    private final SchoolService schoolService;

    @Value("${scoring.fusion.structured:0.60}")
    private double structuredWeight;

    @Value("${scoring.fusion.vector:0.40}")
    private double vectorWeight;

    @Cacheable(key = "#studentId")
    @Transactional(readOnly = true)
    public List<RecommendationResponse> getRecommendations(Long studentId) {
        return recommendationRepository.findByStudentIdOrderByScoreDesc(studentId)
                .stream()
                .map(this::toEnrichedResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(key = "#studentId")
    public List<RecommendationResponse> generateRecommendations(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant introuvable"));
        return generateRecommendations(student);
    }

    @Transactional
    @CacheEvict(key = "#student.id")
    public List<RecommendationResponse> generateRecommendations(Student student) {
        List<Field> fields = fieldRepository.findAll();

        if (fields.isEmpty()) {
            log.warn("Aucune filière disponible");
            return List.of();
        }

        recommendationRepository.deleteByStudentId(student.getId());

        List<Recommendation> recommendations = fields.stream()
                .map(field -> buildRecommendation(student, field))
                .sorted(Comparator.comparingDouble(Recommendation::getScore).reversed())
                .toList();

        List<Recommendation> saved = recommendationRepository.saveAll(recommendations);
        return saved.stream()
                .map(this::toEnrichedResponse)
                .toList();
    }

    private RecommendationResponse toEnrichedResponse(Recommendation recommendation) {
        RecommendationResponse response = recommendationMapper.toResponse(recommendation);
        if (recommendation.getField() != null) {
            response.setSchools(schoolService.findByFieldId(recommendation.getField().getId()));
        }
        return response;
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
            return structuredScore;
        }

        double cosineSimilarity = VectorUtils.cosineSimilarity(studentEmbedding, fieldEmbedding);
        double vectorScore = ((cosineSimilarity + 1) / 2) * 100;

        return (structuredScore * structuredWeight) + (vectorScore * vectorWeight);
    }
}