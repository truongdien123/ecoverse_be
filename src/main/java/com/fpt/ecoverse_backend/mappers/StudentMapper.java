package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mappings({
            @Mapping(target = "studentId", source = "student.id"),
            @Mapping(target = "fullName", source = "user.fullName"),
            @Mapping(target = "studentCode", source = "student.studentCode"),
            @Mapping(target = "grade", source = "student.grade"),
            @Mapping(target = "points", source = "student.points"),
            @Mapping(target = "avatarUrl", source = "user.avatarUrl"),
            @Mapping(target = "createdDate", source = "user.createdAt"),
            @Mapping(target = "updatedDate", source = "user.updatedAt"),
            @Mapping(target = "active", source = "user.active")
    })
    StudentResponseDto toStudentResponse(Student student, User user);

    @Mappings({
            @Mapping(target = "grade", source = "grade")
    })
    Student toStudent(StudentRequestDto request);
}
