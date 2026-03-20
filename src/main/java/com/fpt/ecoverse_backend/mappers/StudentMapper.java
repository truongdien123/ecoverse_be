package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mappings({
            @Mapping(target = "studentId", source = "id"),
            @Mapping(target = "fullName", source = "fullName"),
            @Mapping(target = "studentCode", source = "studentCode"),
            @Mapping(target = "grade", source = "grade"),
            @Mapping(target = "points", source = "points"),
            @Mapping(target = "avatarUrl", source = "avatarUrl"),
            @Mapping(target = "createdDate", source = "createdAt"),
            @Mapping(target = "updatedDate", source = "updatedAt"),
            @Mapping(target = "active", source = "active")
    })
    StudentResponseDto toStudentResponse(Student student);

    @Mappings({
            @Mapping(target = "fullName", source = "fullName"),
            @Mapping(target = "grade", source = "grade")
    })
    Student toStudent(StudentRequestDto request);
}
