package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameAttemptRequestDto {
    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("points_earned")
    private Integer pointsEarned;

    @JsonProperty("total_items")
    private Integer totalItems;

    @JsonProperty("correct_count")
    private Integer correctCount;

    @JsonProperty("completed")
    private Boolean completed;
}
