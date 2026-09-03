package org.example.orientcompanion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.orientcompanion.enums.Role;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@DiscriminatorValue("STUDENT")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Student extends User {

    @Lob
    @Column(name = "interests_json", columnDefinition = "TEXT")
    private String interestsJson;

    @Lob
    @Column(name = "personality_scores_json", columnDefinition = "TEXT")
    private String personalityScoresJson;

    @Lob
    @Column(name = "academic_scores_json", columnDefinition = "TEXT")
    private String academicScoresJson;

    @Lob
    @Column(name = "profile_embedding", columnDefinition = "TEXT")
    private String profileEmbedding;

    @Column(name = "assessment_date")
    private LocalDate assessmentDate;

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }
}