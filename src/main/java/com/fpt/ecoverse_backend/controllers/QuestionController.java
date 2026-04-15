package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.QuestionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizTemplateResponseDto;
import com.fpt.ecoverse_backend.filter.CustomUserDetails;
import com.fpt.ecoverse_backend.services.QuestionService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@PreAuthorize("hasRole('PARTNERSHIP')")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getQuestionsByQuizId(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String quizId
    ) {
        List<QuizTemplateResponseDto.QuestionResponseDto> result = questionService.getQuestionsByQuizId(currentUser.getId(), quizId);
        return ResponseUtil.success("Questions retrieved successfully", result);
    }

    @PostMapping("/quiz/{quizId}")
    public ResponseEntity<?> addQuestionToQuiz(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String quizId,
            @Valid @RequestBody QuestionRequestDto request
    ) {
        QuizTemplateResponseDto.QuestionResponseDto result = questionService.addQuestionToQuiz(currentUser.getId(), quizId, request);
        return ResponseUtil.success("Question created successfully", result);
    }

    @PostMapping("/quiz/{quizId}/AI")
    public ResponseEntity<?> addMultipleQuestionsToQuiz(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String quizId,
            @Valid @RequestBody List<QuestionRequestDto> requests
    ) {
        List<QuizTemplateResponseDto.QuestionResponseDto> result = questionService.addMultipleQuestionsToQuiz(currentUser.getId(), quizId, requests);
        return ResponseUtil.success("Multiple AI questions created successfully", result);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String questionId,
            @Valid @RequestBody QuestionRequestDto request
    ) {
        QuizTemplateResponseDto.QuestionResponseDto result = questionService.updateQuestion(currentUser.getId(), questionId, request);
        return ResponseUtil.success("Question updated successfully", result);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String questionId
    ) {
        questionService.deleteQuestion(currentUser.getId(), questionId);
        return ResponseUtil.success("Question deleted successfully", null);
    }
}
