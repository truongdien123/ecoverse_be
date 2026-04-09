package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;

public interface StudentService {
    StudentResponseDto getStudentDetails(String studentId);
    StudentResponseDto updateStudentDetails(String studentId, StudentRequestDto request);
    StudentResponseDto deleteStudent(String studentId);
}
