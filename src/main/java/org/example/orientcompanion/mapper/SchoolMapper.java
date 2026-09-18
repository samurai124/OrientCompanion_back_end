package org.example.orientcompanion.mapper;

import org.example.orientcompanion.dto.SchoolRequest;
import org.example.orientcompanion.dto.SchoolResponse;
import org.example.orientcompanion.entity.School;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SchoolMapper {

    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "fieldName", source = "field.name")
    SchoolResponse toResponse(School school);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "field", ignore = true)
    void updateFromRequest(SchoolRequest request, @MappingTarget School school);
}
