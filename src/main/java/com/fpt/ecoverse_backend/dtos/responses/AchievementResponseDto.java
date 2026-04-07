package com.fpt.ecoverse_backend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponseDto {
    private String id;
    private String name;
    private String description;
    private Integer pointsRequired;
    private String badgeImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
