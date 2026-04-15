package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionParticipantResponseDto {

    @JsonProperty("competition_participant_id")
    private String competitionParticipantId;

    @JsonProperty("student")
    private StudentResponseDto student;

    @JsonProperty("joined_at")
    private LocalDateTime joinedAt;

    @JsonProperty("total_score")
    private Integer totalScore;
}
