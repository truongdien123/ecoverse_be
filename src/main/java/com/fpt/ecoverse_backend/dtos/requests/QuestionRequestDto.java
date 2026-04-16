package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {

    @NotBlank(message = "Question text is required")
    @JsonProperty("text")
    private String text;

    @NotNull(message = "Options are required")
    @JsonProperty("options")
    private List<String> options;          // ["A. Nhựa", "B. Giấy", "C. Hữu cơ", "D. Khác"]

    @NotBlank(message = "Correct answer is required")
    @JsonProperty("correct_answer")
    private String correctAnswer;          // "A" hoặc "Nhựa" tùy thiết kế

    @JsonProperty("explanation")
    private String explanation;
}
