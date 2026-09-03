package org.example.orientcompanion.service;

import org.example.orientcompanion.util.JsonMapCodec;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Calcule le score structuré (pondération explicite, justifiable devant un jury)
 * entre un étudiant et une filière.
 *
 * Formats JSON attendus :
 * - Student.interestsJson            : {"Mathématiques": 85, "Informatique": 90, ...}   (0-100)
 * - Student.personalityScoresJson    : {"R": 60, "I": 80, "A": 40, "S": 30, "E": 50, "C": 70}  (RIASEC, 0-100)
 * - Student.academicScoresJson       : {"Mathématiques": 16, "Physique": 14, ...}        (notes /20)
 * - Field.requiredTraitsJson         : {"R": 20, "I": 90, "A": 30, "S": 20, "E": 40, "C": 60}  (importance 0-100)
 * - Field.relatedSubjects            : "Mathématiques,Informatique,Physique" (CSV)
 */
@Service
public class ScoringService {

    @Value("${scoring.weights.interest:0.30}")
    private double interestWeight;

    @Value("${scoring.weights.personality:0.40}")
    private double personalityWeight;

    @Value("${scoring.weights.academic:0.30}")
    private double academicWeight;

    /**
     * Score structuré final, normalisé entre 0 et 100.
     */
    public double calculateStructuredScore(Student student, Field field) {
        double interest = interestScore(student, field);
        double personality = personalityScore(student, field);
        double academic = academicScore(student, field);

        return (interest * interestWeight)
                + (personality * personalityWeight)
                + (academic * academicWeight);
    }

    /**
     * Moyenne des scores d'intérêt de l'étudiant pour les matières liées à la filière.
     */
    private double interestScore(Student student, Field field) {
        Map<String, Double> interests = JsonMapCodec.fromJson(student.getInterestsJson());
        Set<String> relatedSubjects = parseCsv(field.getRelatedSubjects());

        if (relatedSubjects.isEmpty() || interests.isEmpty()) {
            return 0.0;
        }

        return relatedSubjects.stream()
                .map(subject -> interests.getOrDefault(subject, 0.0))
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Compatibilité RIASEC : moyenne pondérée des écarts entre le profil de
     * personnalité de l'étudiant et les traits requis par la filière.
     * Plus l'écart est faible sur un trait important, meilleur est le score.
     */
    private double personalityScore(Student student, Field field) {
        Map<String, Double> studentTraits = JsonMapCodec.fromJson(student.getPersonalityScoresJson());
        Map<String, Double> requiredTraits = JsonMapCodec.fromJson(field.getRequiredTraitsJson());

        if (studentTraits.isEmpty() || requiredTraits.isEmpty()) {
            return 0.0;
        }

        double weightedMatchSum = 0.0;
        double totalImportance = 0.0;

        for (Map.Entry<String, Double> entry : requiredTraits.entrySet()) {
            String trait = entry.getKey();
            double importance = entry.getValue();
            double studentValue = studentTraits.getOrDefault(trait, 0.0);

            double gap = Math.abs(importance - studentValue);
            double match = Math.max(0.0, 100.0 - gap);

            weightedMatchSum += match * importance;
            totalImportance += importance;
        }

        return totalImportance == 0.0 ? 0.0 : weightedMatchSum / totalImportance;
    }

    /**
     * Moyenne des notes académiques de l'étudiant pour les matières liées à
     * la filière, ramenée sur 100 (notes stockées sur 20).
     */
    private double academicScore(Student student, Field field) {
        Map<String, Double> academicScores = JsonMapCodec.fromJson(student.getAcademicScoresJson());
        Set<String> relatedSubjects = parseCsv(field.getRelatedSubjects());

        if (relatedSubjects.isEmpty() || academicScores.isEmpty()) {
            return 0.0;
        }

        double averageOn20 = relatedSubjects.stream()
                .map(subject -> academicScores.getOrDefault(subject, 0.0))
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return (averageOn20 / 20.0) * 100.0;
    }

    private Set<String> parseCsv(String csv) {
        if (csv == null || csv.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}