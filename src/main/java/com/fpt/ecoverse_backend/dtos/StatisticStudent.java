package com.fpt.ecoverse_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticStudent {
    @JsonProperty("total_games_played")
    private int totalGamesPlayed;

    @JsonProperty("total_average_accuracy")
    private double totalAverageAccuracy;

    @JsonProperty("total_quizzes_completed")
    private int totalQuizzesCompleted;

    @JsonProperty("total_achievements_unlocked")
    private int totalAchievementsUnlocked;
}
