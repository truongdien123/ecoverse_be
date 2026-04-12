package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.ParentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;

import java.util.List;

public interface ParentService {
    ParentResponseDto linkParentToStudent(String parentId, String studentCode);
    List<StudentResponseDto> getListStudent(String parentId);
    ParentResponseDto getParentDetail(String parentId);
    ParentResponseDto updateParent(String parentId, ParentRequestDto request);
    ParentResponseDto deleteParent(String parentId);
}
