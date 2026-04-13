package com.fpt.ecoverse_backend.dtos.requests;

import com.fpt.ecoverse_backend.enums.CompetitionStatus;
import com.fpt.ecoverse_backend.enums.TargetScope;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionRequestDto {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private CompetitionStatus status;
    private TargetScope scope;
    private String targetClass;
}
