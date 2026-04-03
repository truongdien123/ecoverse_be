package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequestDto {

    @NotNull(message = "Quiz template ID is required")
    @JsonProperty("quiz_template_id")
    private String quizTemplateId;

    @NotNull(message = "Duration is required")
    @JsonProperty("duration")
    private Integer duration;   // Thời gian làm bài (giây)

    @NotEmpty(message = "Answers are required")
    @JsonProperty("answers")
    private List<AnswerDto> answers;

    // ── Inner DTO ──────────────────────────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDto {

        @NotNull(message = "Question ID is required")
        @JsonProperty("question_id")
        private String questionId;

        @JsonProperty("selected_answer")
        private String selectedAnswer;  // có thể null nếu bỏ qua
    }
}
