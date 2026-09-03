package org.example.orientcompanion.mapper;

import org.example.orientcompanion.dto.FieldResponse;
import org.example.orientcompanion.entity.Field;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FieldMapper {

    FieldResponse toResponse(Field field);
}