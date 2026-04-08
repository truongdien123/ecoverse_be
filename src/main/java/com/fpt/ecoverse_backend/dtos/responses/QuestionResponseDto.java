package com.fpt.ecoverse_backend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private String id;
    private String text;
    private String optionsJson;
    private String correctAnswer;
    private String explanation;
    private String quizTemplateId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
