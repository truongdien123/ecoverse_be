package com.fpt.ecoverse_backend.dtos.responses.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptStatsResponseDto {
    private String createdDay;
    private long quizAttempts;
    private long gameAttempts;
}
