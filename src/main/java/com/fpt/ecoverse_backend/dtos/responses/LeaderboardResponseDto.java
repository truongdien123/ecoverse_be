package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardResponseDto {
    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("grade")
    private String grade;

    @JsonProperty("points")
    private Integer points;

    @JsonProperty("min_duration")
    private Integer minDuration;

}
