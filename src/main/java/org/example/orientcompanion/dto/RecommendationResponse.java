package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponse {

    private Long id;
    private Long fieldId;
    private String fieldName;
    private String fieldCategory;
    private double score;
    private String explanation;
    private LocalDateTime createdAt;

    /** Schools that offer this recommended field. */
    private List<SchoolResponse> schools;
}