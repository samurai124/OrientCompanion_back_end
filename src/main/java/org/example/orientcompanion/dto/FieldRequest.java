package org.example.orientcompanion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldRequest {

    @NotBlank(message = "Le nom de la filière est obligatoire")
    private String name;

    private String description;

    private String requiredTraitsJson;

    private String category;

    private String relatedSubjects;
}