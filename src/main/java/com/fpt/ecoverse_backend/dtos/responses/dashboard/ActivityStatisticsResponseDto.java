package com.fpt.ecoverse_backend.dtos.responses.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStatisticsResponseDto {
    private long gameAttemptsIn24h;
    private long quizAttemptsIn24h;
}
