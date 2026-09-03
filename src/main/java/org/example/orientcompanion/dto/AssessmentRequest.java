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

    /** Ex: {"Mathématiques": 85, "Informatique": 90} — score 0-100 */
    @NotEmpty(message = "Les intérêts sont obligatoires")
    private Map<String, Double> interests;

    /** Ex: {"R": 60, "I": 80, "A": 40, "S": 30, "E": 50, "C": 70} — RIASEC 0-100 */
    @NotEmpty(message = "Le profil de personnalité est obligatoire")
    private Map<String, Double> personalityScores;

    /** Ex: {"Mathématiques": 16, "Physique": 14} — notes /20 */
    @NotEmpty(message = "Les résultats académiques sont obligatoires")
    private Map<String, Double> academicScores;
}