package org.example.orientcompanion.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRequest {

    @NotEmpty(message = "Les intérêts sont obligatoires")
    private Map<String, Double> interests;

    @NotEmpty(message = "Le profil de personnalité est obligatoire")
    private Map<String, Double> personalityScores;

    @NotEmpty(message = "Les résultats académiques sont obligatoires")
    private Map<String, Double> academicScores;
}