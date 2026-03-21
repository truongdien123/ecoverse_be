package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverse_backend.dtos.StatisticStudent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("student_code")
    private String studentCode;

    @JsonProperty("grade")
    private String grade;

    @JsonProperty("points")
    private int points;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("parent")
    private ParentResponseDto parent;

    @JsonProperty("statistics")
    private StatisticStudent statistics;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;

    @JsonProperty("active")
    private boolean active;
}
