package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;

public interface ParentService {
    ParentResponseDto linkParentToStudent(String parentId, String studentId);
}
