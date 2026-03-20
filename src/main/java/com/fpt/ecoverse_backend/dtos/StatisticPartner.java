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
public class StatisticPartner {

    @JsonProperty("total_students")
    private long totalStudents;

    @JsonProperty("total_parents")
    private long totalParents;

    @JsonProperty("total_active_games")
    private long totalActiveGames;

    @JsonProperty("total_active_quizzes")
    private long totalActiveQuizzes;

    @JsonProperty("total_points_distributed")
    private long totalPointDistributed;

    @JsonProperty("total_redemptions")
    private long totalRedemptions;
}