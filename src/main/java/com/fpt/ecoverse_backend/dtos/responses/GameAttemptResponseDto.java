package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameAttemptResponseDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("game_round_id")
    private String gameRoundId;

    @JsonProperty("title_game_round")
    private String titleGameRound;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("points_earned")
    private Integer pointsEarned;

    @JsonProperty("correct_count")
    private Integer correctCount;

    @JsonProperty("total_items")
    private Integer totalItems;

    @JsonProperty("attempt_number")
    private Integer attemptNumber;

    @JsonProperty("completed")
    private Boolean completed;
}
