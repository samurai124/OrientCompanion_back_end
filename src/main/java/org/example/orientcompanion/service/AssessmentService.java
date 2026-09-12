package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orientcompanion.dto.AssessmentRequest;
import org.example.orientcompanion.dto.StudentProfileResponse;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.repository.StudentRepository;
import org.example.orientcompanion.util.EmbeddingCodec;
import org.example.orientcompanion.util.JsonMapCodec;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "student_profiles")
public class AssessmentService {

    private final StudentRepository studentRepository;
    private final EmbeddingService embeddingService;
    private final RecommendationService recommendationService;

    @Transactional
    @CacheEvict(key = "#studentId")
    public StudentProfileResponse submitAssessment(Long studentId, AssessmentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant introuvable"));

        student.setInterestsJson(JsonMapCodec.toJson(request.getInterests()));
        student.setPersonalityScoresJson(JsonMapCodec.toJson(request.getPersonalityScores()));
        student.setAcademicScoresJson(JsonMapCodec.toJson(request.getAcademicScores()));
        student.setAssessmentDate(LocalDate.now());

        float[] embedding = embeddingService.embed(buildProfileText(request));
        if (embedding != null) {
            student.setProfileEmbedding(EmbeddingCodec.toJson(embedding));
        } else {
            log.warn("Embedding non calculé pour l'étudiant {} — le scoring vectoriel sera ignoré", studentId);
        }

        student = studentRepository.save(student);
        recommendationService.generateRecommendations(student);

        return toResponse(student);
    }

    @Cacheable(key = "#studentId")
    public StudentProfileResponse getProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant introuvable"));

        return toResponse(student);
    }

    private String buildProfileText(AssessmentRequest request) {
        String interestsText = request.getInterests().entrySet().stream()
                .map(e -> e.getKey() + " (" + e.getValue() + ")")
                .collect(Collectors.joining(", "));

        String personalityText = request.getPersonalityScores().entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));

        return "Intérêts : %s. Profil de personnalité RIASEC : %s.".formatted(interestsText, personalityText);
    }

    private StudentProfileResponse toResponse(Student student) {
        return StudentProfileResponse.builder()
                .id(student.getId())
                .interests(JsonMapCodec.fromJson(student.getInterestsJson()))
                .personalityScores(JsonMapCodec.fromJson(student.getPersonalityScoresJson()))
                .academicScores(JsonMapCodec.fromJson(student.getAcademicScoresJson()))
                .assessmentDate(student.getAssessmentDate())
                .embeddingComputed(student.getProfileEmbedding() != null)
                .build();
    }
}