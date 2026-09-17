package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSummaryDTO implements Serializable {
    private Long id;
    private String fullName;
    private String email;
    private String educationLevel;
    private String city;
    private String dominantRiasec;
    private LocalDate assessmentDate;
    private boolean assessmentDone;
}
