package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldResponse {

    private Long id;
    private String name;
    private String description;
    private String requiredTraitsJson;
    private String category;
    private String relatedSubjects;
}