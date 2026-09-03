package org.example.orientcompanion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.orientcompanion.enums.SessionStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipSessionUpdateRequest {

    @NotNull(message = "Le statut est obligatoire")
    private SessionStatus status;

    /** Requis si status = SCHEDULED */
    private LocalDateTime scheduledAt;
}