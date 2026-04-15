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
public class QuizTemplateResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("question_count")
    private int questionCount;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("partner_name")
    private String partnerName;

    @JsonProperty("is_competition")
    private Boolean isCompetition;

    @JsonProperty("questions")
    private List<QuestionResponseDto> questions;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // ── Inner DTO ─────────────────────────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionResponseDto {

        @JsonProperty("id")
        private String id;

        @JsonProperty("text")
        private String text;

        @JsonProperty("options")
        private List<String> options;

        @JsonProperty("correct_answer")
        private String correctAnswer;

        @JsonProperty("explanation")
        private String explanation;
    }
}
