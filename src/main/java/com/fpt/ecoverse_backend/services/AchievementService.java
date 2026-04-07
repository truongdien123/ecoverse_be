package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.AchievementRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.AchievementResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AchievementService {
    AchievementResponseDto createAchievement(AchievementRequestDto requestDto);
    AchievementResponseDto getAchievementById(String id);
    Page<AchievementResponseDto> getAllAchievements(Pageable pageable);
    AchievementResponseDto updateAchievement(String id, AchievementRequestDto requestDto);
    void deleteAchievement(String id);
}
