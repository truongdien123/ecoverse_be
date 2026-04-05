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
public class StudentRequestDto {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("grade")
    private String grade;

    @JsonProperty("class_number")
    private String classNumber;

}
