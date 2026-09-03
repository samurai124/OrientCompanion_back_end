package org.example.orientcompanion.mapper;

import org.example.orientcompanion.dto.CounselorProfileResponse;
import org.example.orientcompanion.entity.Counselor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CounselorMapper {

    @Mapping(target = "specialtyFieldId", source = "specialtyField.id")
    @Mapping(target = "specialtyFieldName", source = "specialtyField.name")
    CounselorProfileResponse toResponse(Counselor counselor);
}