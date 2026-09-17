package org.example.orientcompanion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserStatusUpdateRequest(

        @NotBlank(message = "Le statut ne peut pas être vide")
        @Pattern(
                regexp = "^(ACTIVE|SUSPENDED)$",
                message = "Le statut doit être 'ACTIVE' ou 'SUSPENDED'"
        )
        String status
) {}
