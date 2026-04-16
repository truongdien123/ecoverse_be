package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverse_backend.enums.CompetitionStatus;
import com.fpt.ecoverse_backend.enums.TargetScope;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionResponseDto {
    @JsonProperty("competition_id")
    private String competitionId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @JsonProperty("status")
    private CompetitionStatus status;

    @JsonProperty("scope")
    private TargetScope scope;

    @JsonProperty("target_class")
    private String targetClass;

    @JsonProperty("game_round")
    private GameRoundResponseDto gameRound;

    @JsonProperty("quiz_template")
    private QuizTemplateResponseDto quizTemplate;
}
