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

@Service
public class ScoringService {

    @Value("${scoring.weights.interest:0.30}")
    private double interestWeight;

    @Value("${scoring.weights.personality:0.40}")
    private double personalityWeight;

    @Value("${scoring.weights.academic:0.30}")
    private double academicWeight;

    public double calculateStructuredScore(Student student, Field field) {
        double interest = averageForSubjects(student.getInterestsJson(), field.getRelatedSubjects());
        double academic = averageForSubjects(student.getAcademicScoresJson(), field.getRelatedSubjects()) / 20.0 * 100.0;
        double personality = personalityScore(student, field);

        return interest * interestWeight + personality * personalityWeight + academic * academicWeight;
    }

    private double averageForSubjects(String scoresJson, String relatedSubjectsCsv) {
        Map<String, Double> scores = JsonMapCodec.fromJson(scoresJson);
        Set<String> subjects = parseCsv(relatedSubjectsCsv);

        if (subjects.isEmpty() || scores.isEmpty()) {
            return 0.0;
        }

        return subjects.stream()
                .mapToDouble(subject -> scores.getOrDefault(subject, 0.0))
                .average()
                .orElse(0.0);
    }

    private double personalityScore(Student student, Field field) {
        Map<String, Double> studentTraits = JsonMapCodec.fromJson(student.getPersonalityScoresJson());
        Map<String, Double> requiredTraits = JsonMapCodec.fromJson(field.getRequiredTraitsJson());

        if (studentTraits.isEmpty() || requiredTraits.isEmpty()) {
            return 0.0;
        }

        double weightedMatchSum = 0.0;
        double totalImportance = 0.0;

        for (Map.Entry<String, Double> entry : requiredTraits.entrySet()) {
            double importance = entry.getValue();
            double gap = Math.abs(importance - studentTraits.getOrDefault(entry.getKey(), 0.0));
            weightedMatchSum += Math.max(0.0, 100.0 - gap) * importance;
            totalImportance += importance;
        }

        return totalImportance == 0.0 ? 0.0 : weightedMatchSum / totalImportance;
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