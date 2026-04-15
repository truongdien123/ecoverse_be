package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.QuizSubmitRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.QuizTemplateRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizAttemptResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizTemplateResponseDto;
import com.fpt.ecoverse_backend.filter.CustomUserDetails;
import com.fpt.ecoverse_backend.services.QuizService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // ════════════════════════════════════════════════════════════════
    // PARTNER: Quản lý Quiz Template
    // ════════════════════════════════════════════════════════════════

    /**
     * Tạo quiz template mới
     * POST /api/quiz/templates
     */
    @PostMapping("/templates")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> createQuizTemplate(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody QuizTemplateRequestDto request
    ) {
        QuizTemplateResponseDto result = quizService.createQuizTemplate(currentUser.getId(), request);
        return ResponseUtil.success("Quiz template created successfully", result);
    }

    /**
     * Cập nhật quiz template
     * PUT /api/quiz/templates/{templateId}
     */
    @PutMapping("/templates/{templateId}")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> updateQuizTemplate(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String templateId,
            @Valid @RequestBody QuizTemplateRequestDto request
    ) {
        QuizTemplateResponseDto result = quizService.updateQuizTemplate(currentUser.getId(), templateId, request);
        return ResponseUtil.success("Quiz template updated successfully", result);
    }

    /**
     * Xoá (soft delete) quiz template
     * DELETE /api/quiz/templates/{templateId}
     */
    @DeleteMapping("/templates/{templateId}")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> deleteQuizTemplate(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String templateId
    ) {
        quizService.deleteQuizTemplate(currentUser.getId(), templateId);
        return ResponseUtil.success("Quiz template deleted successfully", null);
    }

    /**
     * Danh sách quiz của partner mình
     * GET /api/quiz/templates/my?title=&page=0&size=10
     */
    @GetMapping("/templates/my")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> getMyQuizTemplates(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<QuizTemplateResponseDto> result = quizService.getMyQuizTemplates(currentUser.getId(), title, page, size);
        return ResponseUtil.success("My quiz templates", result);
    }

    /**
     * Chi tiết quiz (dành cho partner xem câu hỏi + đáp án đúng)
     * GET /api/quiz/templates/{templateId}
     */
    @GetMapping("/templates/{templateId}")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> getQuizTemplateDetail(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String templateId
    ) {
        QuizTemplateResponseDto result = quizService.getQuizTemplateDetail(currentUser.getId(), templateId);
        return ResponseUtil.success("Quiz template detail", result);
    }

    // ════════════════════════════════════════════════════════════════
    // STUDENT: Làm bài Quiz
    // ════════════════════════════════════════════════════════════════

    /**
     * Danh sách quiz available (ẩn đáp án đúng)
     * GET /api/quiz/available?partnerId=&title=&page=0&size=10
     */
    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT')")
    public ResponseEntity<?> getAvailableQuizzes(
            @RequestParam(required = false) String partnerId,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<QuizTemplateResponseDto> result = quizService.getAvailableQuizzes(partnerId, title, page, size);
        return ResponseUtil.success("Available quizzes", result);
    }

    /**
     * Chi tiết quiz để student làm bài (ẩn đáp án đúng)
     * GET /api/quiz/{templateId}/start
     */
    @GetMapping("/{templateId}/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getQuizForStudent(@PathVariable String templateId) {
        QuizTemplateResponseDto result = quizService.getQuizForStudent(templateId);
        return ResponseUtil.success("Quiz detail", result);
    }

    /**
     * Student submit bài làm → hệ thống chấm điểm
     * POST /api/quiz/submit
     */
    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitQuiz(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody QuizSubmitRequestDto request
    ) {
        QuizAttemptResponseDto result = quizService.submitQuiz(currentUser.getId(), request);
        return ResponseUtil.success("Quiz submitted successfully", result);
    }

    /**
     * Lịch sử làm bài của student
     * GET /api/quiz/my-attempts?page=0&size=10
     */
    @GetMapping("/my-attempts")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getMyAttempts(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<QuizAttemptResponseDto> result = quizService.getMyAttempts(currentUser.getId(), page, size);
        return ResponseUtil.success("My quiz history", result);
    }

    @GetMapping("/partners/{partner_id}/competitions")
    @PreAuthorize("hasAnyRole('PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getQuizTemplateCompetition(@PathVariable("partner_id") String partnerId) {
        return ResponseUtil.success("Get list quiz template successfully", quizService.getQuizTemplateCompetition(partnerId));
    }
}
