package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.base.ApiResponse;
import com.fpt.ecoverse_backend.dtos.requests.QuestionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuestionResponseDto;
import com.fpt.ecoverse_backend.services.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponseDto>> createQuestion(
            @Valid @RequestBody QuestionRequestDto requestDto) {
        QuestionResponseDto created = questionService.createQuestion(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Thêm câu hỏi thành công", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDto>> getQuestionById(@PathVariable String id) {
        QuestionResponseDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin câu hỏi thành công", question));
    }

    @GetMapping("/quiz-template/{quizTemplateId}")
    public ResponseEntity<ApiResponse<List<QuestionResponseDto>>> getQuestionsByQuizTemplateId(@PathVariable String quizTemplateId) {
        List<QuestionResponseDto> questions = questionService.getQuestionsByQuizTemplateId(quizTemplateId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách câu hỏi thành công", questions));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDto>> updateQuestion(
            @PathVariable String id,
            @Valid @RequestBody QuestionRequestDto requestDto) {
        QuestionResponseDto updated = questionService.updateQuestion(id, requestDto);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật câu hỏi thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(new ApiResponse<>("Xóa câu hỏi thành công", null));
    }
}
