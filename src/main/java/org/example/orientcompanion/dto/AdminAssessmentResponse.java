package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAssessmentResponse implements Serializable {

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private LocalDate assessmentDate;
    private boolean embeddingComputed;

    private String dominantCode;
    private String dominantTitle;
    private String status;
    private Map<String, Double> scores;
    private List<TopRecommendationDTO> topRecommendations;
    private String aiSummary;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopRecommendationDTO implements Serializable {
        private String field;
        private String school;
        private String match;
    }
}
