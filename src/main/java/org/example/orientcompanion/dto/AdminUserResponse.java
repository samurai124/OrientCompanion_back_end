package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.orientcompanion.enums.Role;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserResponse implements Serializable {

    private Long id;
    private String email;
    private String fullName;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdAt;

    private LocalDate assessmentDate;
    private boolean assessmentDone;
}
