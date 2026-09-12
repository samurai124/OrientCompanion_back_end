package org.example.orientcompanion.mapper;

import org.example.orientcompanion.dto.FieldRequest;
import org.example.orientcompanion.dto.FieldResponse;
import org.example.orientcompanion.entity.Field;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FieldMapper {

    FieldResponse toResponse(Field field);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fieldEmbedding", ignore = true)
    Field toEntity(FieldRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fieldEmbedding", ignore = true)
    void updateFromRequest(FieldRequest request, @MappingTarget Field field);
}