package org.example.orientcompanion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.orientcompanion.enums.Role;

public record AdminCreateUserRequest(

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        String password,

        @NotBlank(message = "Le nom complet est obligatoire")
        String fullName,

        @NotNull(message = "Le rôle est obligatoire")
        Role role
) {}
