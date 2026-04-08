package com.fpt.ecoverse_backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String text;

    @NotEmpty(message = "Danh sách lựa chọn không được để trống")
    private List<String> options;

    @NotBlank(message = "Đáp án đúng không được để trống")
    private String correctAnswer;

    private String explanation;
}
