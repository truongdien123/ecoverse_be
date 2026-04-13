package com.fpt.ecoverse_backend.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionParticipantRequestDto {

    private LocalDateTime joinedAt;
    private Integer totalScore;
}
