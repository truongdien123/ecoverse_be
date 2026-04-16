package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    StudentResponseDto getStudentDetails(String studentId);
    StudentResponseDto updateStudentDetails(String studentId, StudentRequestDto request);
    StudentResponseDto deleteStudent(String studentId);
    List<WasteItemResponseDto> getWasteItemByAI(String studentId);
    StudentResponseDto updateAvatar(String studentId, MultipartFile file);
}
