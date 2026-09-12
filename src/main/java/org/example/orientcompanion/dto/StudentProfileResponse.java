package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfileResponse implements Serializable {

    private Long id;
    private Map<String, Double> interests;
    private Map<String, Double> personalityScores;
    private Map<String, Double> academicScores;
    private LocalDate assessmentDate;
    private boolean embeddingComputed;
}