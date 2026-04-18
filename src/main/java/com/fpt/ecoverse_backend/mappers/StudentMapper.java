package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mappings({
            @Mapping(target = "studentId", source = "id"),
            @Mapping(target = "fullName", source = "user.fullName"),
            @Mapping(target = "studentCode", source = "studentCode"),
            @Mapping(target = "grade", source = "grade"),
            @Mapping(target = "points", source = "points"),
            @Mapping(target = "avatarUrl", source = "user.avatarUrl"),
            @Mapping(target = "createdDate", source = "user.createdAt"),
            @Mapping(target = "updatedDate", source = "user.updatedAt"),
            @Mapping(target = "active", source = "user.active")
    })
    StudentResponseDto toStudentResponse(Student student);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "grade", source = "request.grade"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "studentCode", ignore = true),
            @Mapping(target = "points", ignore = true)
    })
    void updateStudent(@MappingTarget Student student, StudentRequestDto request);

    @Mappings({
            @Mapping(target = "grade", source = "request.grade"),
            @Mapping(target = "id", source = "id")
    })
    Student toStudent(StudentRequestDto request, String id);
}
