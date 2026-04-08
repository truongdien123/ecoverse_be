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

    @JsonProperty("description")
    private String description;

    @NotEmpty(message = "Questions list must not be empty")
    @JsonProperty("questions")
    private List<QuestionRequestDto> questions;

    // ── Inner DTO ──────────────────────────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionRequestDto {

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
}
