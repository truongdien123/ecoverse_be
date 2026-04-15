package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.QuizSubmitRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.QuizTemplateRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizAttemptResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizTemplateResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuizService {

    // ─── PARTNER: Quản lý quiz template ───────────────────────────────

    /** Tạo mới quiz template (PARTNER) */
    QuizTemplateResponseDto createQuizTemplate(String partnerId, QuizTemplateRequestDto request);

    /** Cập nhật quiz template (chỉ partner sở hữu) */
    QuizTemplateResponseDto updateQuizTemplate(String partnerId, String templateId, QuizTemplateRequestDto request);

    /** Xoá mềm quiz template (chỉ partner sở hữu) */
    void deleteQuizTemplate(String partnerId, String templateId);

    /** Xem danh sách quiz của partner mình */
    Page<QuizTemplateResponseDto> getMyQuizTemplates(String partnerId, String title, int page, int size);

    /** Xem chi tiết một quiz template (có câu hỏi) */
    QuizTemplateResponseDto getQuizTemplateDetail(String partnerId, String templateId);

    // ─── STUDENT: Làm bài quiz ─────────────────────────────────────────

    /** Lấy danh sách quiz active (không kèm đáp án đúng) */
    Page<QuizTemplateResponseDto> getAvailableQuizzes(String partnerId, String title, int page, int size);

    /** Lấy chi tiết quiz để làm bài (không kèm đáp án đúng) */
    QuizTemplateResponseDto getQuizForStudent(String templateId);

    /** Submit bài quiz → hệ thống chấm điểm, cộng điểm student */
    QuizAttemptResponseDto submitQuiz(String studentId, QuizSubmitRequestDto request);

    /** Xem lịch sử làm bài của student */
    Page<QuizAttemptResponseDto> getMyAttempts(String studentId, int page, int size);

    List<QuizTemplateResponseDto> getQuizTemplateCompetition(String partnerId);
}
