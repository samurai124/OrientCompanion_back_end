package org.example.orientcompanion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipSessionRequest {

    @NotNull(message = "Le conseiller est obligatoire")
    private Long counselorId;
}