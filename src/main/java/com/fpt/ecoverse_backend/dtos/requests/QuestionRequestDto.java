package com.fpt.ecoverse_backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String text;

    @NotBlank(message = "Danh sách lựa chọn (JSON) không được để trống")
    private String optionsJson;

    @NotBlank(message = "Đáp án đúng không được để trống")
    private String correctAnswer;

    private String explanation;

    @NotBlank(message = "ID bộ đề không được để trống")
    private String quizTemplateId;
}
