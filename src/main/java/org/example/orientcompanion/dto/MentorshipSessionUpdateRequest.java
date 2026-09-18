package org.example.orientcompanion.dto;

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

    private SessionStatus status;

    private LocalDateTime scheduledAt;

    private String meetLink;
}