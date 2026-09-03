package org.example.orientcompanion.mapper;

import org.example.orientcompanion.dto.UserResponse;
import org.example.orientcompanion.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole())")
    UserResponse toResponse(User user);
}