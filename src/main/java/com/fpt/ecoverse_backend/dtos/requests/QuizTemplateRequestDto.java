package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizTemplateRequestDto {

    @NotBlank(message = "Title is required")
    @JsonProperty("title")
    private String title;

    @JsonProperty("listId_question")
    private List<String> listIdQuestion;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_competition")
    private Boolean isCompetition;

}
