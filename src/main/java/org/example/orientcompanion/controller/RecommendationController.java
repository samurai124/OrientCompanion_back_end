package org.example.orientcompanion.controller;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.RecommendationResponse;
import org.example.orientcompanion.dto.SchoolResponse;
import org.example.orientcompanion.entity.Recommendation;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.mapper.RecommendationMapper;
import org.example.orientcompanion.repository.RecommendationRepository;
import org.example.orientcompanion.repository.SchoolRepository;
import org.example.orientcompanion.repository.StudentRepository;
import org.example.orientcompanion.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/recommendations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class RecommendationController {

    private final RecommendationRepository recommendationRepository;
    private final RecommendationService recommendationService;
    private final StudentRepository studentRepository;
    private final RecommendationMapper recommendationMapper;
    private final SchoolRepository schoolRepository;

    @GetMapping
    public ResponseEntity<List<RecommendationResponse>> getMyRecommendations(
            @AuthenticationPrincipal User principal
    ) {
        List<Recommendation> recommendations =
                recommendationRepository.findByStudentIdOrderByScoreDesc(principal.getId());

        return ResponseEntity.ok(recommendations.stream().map(this::toEnrichedResponse).toList());
    }

    /**
     * Régénère les recommandations sans repasser le bilan complet — utile
     * si le catalogue de filières a été mis à jour depuis la dernière
     * génération.
     */
    @PostMapping("/regenerate")
    public ResponseEntity<List<RecommendationResponse>> regenerate(@AuthenticationPrincipal User principal) {
        Student student = studentRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant introuvable"));

        List<Recommendation> recommendations = recommendationService.generateRecommendations(student);

        return ResponseEntity.ok(recommendations.stream().map(this::toEnrichedResponse).toList());
    }

    /**
     * Maps a Recommendation to its DTO and populates the list of schools
     * that offer the recommended field.
     */
    private RecommendationResponse toEnrichedResponse(Recommendation recommendation) {
        RecommendationResponse response = recommendationMapper.toResponse(recommendation);

        if (recommendation.getField() != null) {
            List<SchoolResponse> schools = schoolRepository
                    .findByFieldId(recommendation.getField().getId())
                    .stream()
                    .map(school -> SchoolResponse.builder()
                            .id(school.getId())
                            .name(school.getName())
                            .city(school.getCity())
                            .country(school.getCountry())
                            .type(school.getType())
                            .website(school.getWebsite())
                            .description(school.getDescription())
                            .build())
                    .toList();
            response.setSchools(schools);
        }

        return response;
    }
}