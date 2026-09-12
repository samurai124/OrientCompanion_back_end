package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.orientcompanion.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {

    private Long id;
    private String email;
    private String fullName;
    private Role role;
    private LocalDateTime createdAt;
}