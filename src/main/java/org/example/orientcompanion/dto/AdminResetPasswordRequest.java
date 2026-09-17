package org.example.orientcompanion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminResetPasswordRequest(

        @NotBlank(message = "Le nouveau mot de passe ne peut pas être vide")
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        String newPassword
) {}
