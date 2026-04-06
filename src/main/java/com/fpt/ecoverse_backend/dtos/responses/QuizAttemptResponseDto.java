package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptResponseDto {

    @JsonProperty("attempt_id")
    private String attemptId;

    @JsonProperty("quiz_template_id")
    private String quizTemplateId;

    @JsonProperty("quiz_title")
    private String quizTitle;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("score")
    private Integer score;

    @JsonProperty("correct_amount")
    private Integer correctAmount;

    @JsonProperty("wrong_amount")
    private Integer wrongAmount;

    @JsonProperty("total_questions")
    private Integer totalQuestions;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("attempt_number")
    private Integer attemptNumber;

    @JsonProperty("completed")
    private Boolean completed;

    @JsonProperty("placements")
    private List<PlacementDto> placements;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    // ── Inner DTO ─────────────────────────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlacementDto {

        @JsonProperty("question_id")
        private String questionId;

        @JsonProperty("question_text")
        private String questionText;

        @JsonProperty("selected_answer")
        private String selectedAnswer;

        @JsonProperty("correct_answer")
        private String correctAnswer;

        @JsonProperty("is_correct")
        private Boolean isCorrect;
    }
}
