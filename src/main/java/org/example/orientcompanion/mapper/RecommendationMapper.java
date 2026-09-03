package org.example.orientcompanion.mapper;

import org.example.orientcompanion.dto.RecommendationResponse;
import org.example.orientcompanion.entity.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "fieldName", source = "field.name")
    @Mapping(target = "fieldCategory", source = "field.category")
    @Mapping(target = "schools", ignore = true)
    RecommendationResponse toResponse(Recommendation recommendation);
}