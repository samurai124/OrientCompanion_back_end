package org.example.orientcompanion.mapper;

import org.example.orientcompanion.dto.MentorshipSessionResponse;
import org.example.orientcompanion.entity.MentorshipSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MentorshipMapper {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.fullName")
    @Mapping(target = "studentEmail", source = "student.email")
    @Mapping(target = "counselorId", source = "counselor.id")
    @Mapping(target = "counselorName", source = "counselor.fullName")
    @Mapping(target = "counselorEmail", source = "counselor.email")
    MentorshipSessionResponse toResponse(MentorshipSession session);
}