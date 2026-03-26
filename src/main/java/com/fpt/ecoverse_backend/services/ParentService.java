package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;

import java.util.List;

public interface ParentService {
    ParentResponseDto linkParentToStudent(String parentId, String studentId);
    List<StudentResponseDto> getListStudent(String parentId);
}
