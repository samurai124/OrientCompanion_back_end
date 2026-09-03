package org.example.orientcompanion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolRequest {

    @NotBlank(message = "Le nom de l'école est obligatoire")
    private String name;

    private String city;

    private String country;

    private String type;

    private String website;

    private String description;

    @NotNull(message = "L'identifiant de la filière est obligatoire")
    private Long fieldId;
}
