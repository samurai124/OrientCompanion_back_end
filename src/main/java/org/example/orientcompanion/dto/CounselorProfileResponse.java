package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CounselorProfileResponse implements Serializable {

    private Long id;
    private String fullName;
    private String email;
    private String bio;
    private Long specialtyFieldId;
    private String specialtyFieldName;
}