package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.orientcompanion.enums.SessionStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorshipSessionResponse implements Serializable {

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long counselorId;
    private String counselorName;
    private String counselorEmail;
    private SessionStatus status;
    private LocalDateTime scheduledAt;
    private String meetLink;
    private LocalDateTime createdAt;
}