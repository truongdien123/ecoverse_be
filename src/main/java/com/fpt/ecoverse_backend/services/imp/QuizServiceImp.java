package com.fpt.ecoverse_backend.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoverse_backend.dtos.requests.QuizSubmitRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.QuizTemplateRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizAttemptResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizTemplateResponseDto;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import com.fpt.ecoverse_backend.enums.LeaderboardScope;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.ForbiddenException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.LeaderboardService;
import com.fpt.ecoverse_backend.services.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuizServiceImp implements QuizService {

    private final QuizTemplateRepository quizTemplateRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizPlacementRepository quizPlacementRepository;
    private final PartnerRepository partnerRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;
    private final LeaderboardService leaderboardService;

    public QuizServiceImp(QuizTemplateRepository quizTemplateRepository,
                          QuizAttemptRepository quizAttemptRepository,
                          QuizPlacementRepository quizPlacementRepository,
                          PartnerRepository partnerRepository,
                          StudentRepository studentRepository,
                          ObjectMapper objectMapper, LeaderboardService leaderboardService) {
        this.quizTemplateRepository = quizTemplateRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizPlacementRepository = quizPlacementRepository;
        this.partnerRepository = partnerRepository;
        this.studentRepository = studentRepository;
        this.objectMapper = objectMapper;
        this.leaderboardService = leaderboardService;
    }

    // ════════════════════════════════════════════════════════════════
    // PARTNER: Quản lý quiz template
    // ════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public QuizTemplateResponseDto createQuizTemplate(String partnerId, QuizTemplateRequestDto request) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new NotFoundException("Partner not found: " + partnerId));

        QuizTemplate template = new QuizTemplate();
        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());
        template.setCreatedBy(CreatedBy.PARTNERSHIP);
        template.setPartner(partner);
        template.setActive(true);
        template.setIsCompetition(request.getIsCompetition());

        // Tạo danh sách Questions
        List<Question> questions = buildQuestions(request.getQuestions(), template);
        template.setQuestions(questions);

        QuizTemplate saved = quizTemplateRepository.save(template);
        return toDetailResponse(saved, true);
    }

    @Override
    @Transactional
    public QuizTemplateResponseDto updateQuizTemplate(String partnerId, String templateId, QuizTemplateRequestDto request) {
        QuizTemplate template = quizTemplateRepository.findByIdAndPartnerId(templateId, partnerId)
                .orElseThrow(() -> new NotFoundException("Quiz template not found or access denied"));

        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());

        // Xoá câu hỏi cũ, thêm câu hỏi mới
        template.getQuestions().clear();
        template.getQuestions().addAll(buildQuestions(request.getQuestions(), template));

        QuizTemplate saved = quizTemplateRepository.save(template);
        return toDetailResponse(saved, true);
    }

    @Override
    @Transactional
    public void deleteQuizTemplate(String partnerId, String templateId) {
        QuizTemplate template = quizTemplateRepository.findByIdAndPartnerId(templateId, partnerId)
                .orElseThrow(() -> new NotFoundException("Quiz template not found or access denied"));

        // Soft delete
        template.setActive(false);
        quizTemplateRepository.save(template);
    }

    @Override
    public Page<QuizTemplateResponseDto> getMyQuizTemplates(String partnerId, String title, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return quizTemplateRepository.findByPartnerIdAndActiveTrue(partnerId, title, pageable)
                .map(t -> toDetailResponse(t, false));
    }

    @Override
    public QuizTemplateResponseDto getQuizTemplateDetail(String partnerId, String templateId) {
        QuizTemplate template = quizTemplateRepository.findByIdAndPartnerId(templateId, partnerId)
                .orElseThrow(() -> new NotFoundException("Quiz template not found or access denied"));
        return toDetailResponse(template, true);
    }

    // ════════════════════════════════════════════════════════════════
    // STUDENT: Làm bài quiz
    // ════════════════════════════════════════════════════════════════

    @Override
    public Page<QuizTemplateResponseDto> getAvailableQuizzes(String partnerId, String title, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (partnerId != null) {
            return quizTemplateRepository.findByPartnerIdAndActiveTrue(partnerId, title, pageable)
                    .map(t -> toStudentResponse(t));
        }
        return quizTemplateRepository.findAllActive(title, pageable)
                .map(t -> toStudentResponse(t));
    }

    @Override
    public QuizTemplateResponseDto getQuizForStudent(String templateId) {
        QuizTemplate template = quizTemplateRepository.findById(templateId)
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + templateId));
        if (!template.getActive()) {
            throw new NotFoundException("Quiz is not available");
        }
        return toStudentResponse(template);
    }

    @Override
    @Transactional
    public QuizAttemptResponseDto submitQuiz(String studentId, QuizSubmitRequestDto request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

        QuizTemplate template = quizTemplateRepository.findById(request.getQuizTemplateId())
                .orElseThrow(() -> new NotFoundException("Quiz template not found: " + request.getQuizTemplateId()));

        if (!template.getActive()) {
            throw new BadRequestException("This quiz is no longer available");
        }

        // Tính attempt_number
        int attemptNumber = quizAttemptRepository.countByTemplateAndStudent(template.getId(), studentId) + 1;

        // Map questionId → Question từ template
        Map<String, Question> questionMap = template.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        // Tạo QuizAttempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuizTemplate(template);
        attempt.setStudent(student);
        attempt.setDuration(request.getDuration());
        attempt.setAttemptNumber(attemptNumber);
        attempt.setCompleted(true);

        // Chấm điểm + tạo QuizPlacements
        int correct = 0;
        List<QuizPlacement> placements = new ArrayList<>();

        for (QuizSubmitRequestDto.AnswerDto ansDto : request.getAnswers()) {
            Question question = questionMap.get(ansDto.getQuestionId());
            if (question == null) continue;

            boolean isCorrect = question.getCorrectAnswer() != null
                    && question.getCorrectAnswer().equalsIgnoreCase(ansDto.getSelectedAnswer());
            if (isCorrect) correct++;

            QuizPlacement placement = new QuizPlacement();
            placement.setQuizAttempt(attempt);
            placement.setQuestion(question);
            placement.setSelectedAnswer(ansDto.getSelectedAnswer());
            placement.setIsCorrect(isCorrect);
            placements.add(placement);
        }

        int totalQuestions = template.getQuestions().size();
        int wrong = totalQuestions - correct;
        int score = totalQuestions > 0 ? (int) Math.round((double) correct / totalQuestions * 100) : 0;

        attempt.setCorrectAmount(correct);
        attempt.setWrongAmount(wrong);
        attempt.setScore(score);
        attempt.setQuizPlacements(placements);

        // Cộng điểm cho student (10 điểm * số câu đúng)
        int pointsEarned = correct * 10;
        student.setPoints((student.getPoints() != null ? student.getPoints() : 0) + pointsEarned);
        handleQuizResult(attempt, pointsEarned);
        studentRepository.save(student);

        QuizAttempt saved = quizAttemptRepository.save(attempt);
        return toAttemptResponse(saved);
    }

    @Override
    public Page<QuizAttemptResponseDto> getMyAttempts(String studentId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return quizAttemptRepository.findByStudentId(studentId, pageable)
                .map(this::toAttemptResponse);
    }

    @Override
    public List<QuizTemplateResponseDto> getQuizTemplateCompetition(String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        List<QuizTemplate> quizTemplates = quizTemplateRepository.findQuizTemplateCompetition(partnerId);
        return quizTemplates.stream().map(quizTemplate -> toDetailResponse(quizTemplate, true)).toList();
    }

    // ════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ════════════════════════════════════════════════════════════════

    private List<Question> buildQuestions(List<QuizTemplateRequestDto.QuestionRequestDto> dtos, QuizTemplate template) {
        List<Question> questions = new ArrayList<>();
        for (QuizTemplateRequestDto.QuestionRequestDto dto : dtos) {
            Question q = new Question();
            q.setText(dto.getText());
            q.setCorrectAnswer(dto.getCorrectAnswer());
            q.setExplanation(dto.getExplanation());
            q.setQuizTemplate(template);
            try {
                q.setOptionsJson(objectMapper.writeValueAsString(dto.getOptions()));
            } catch (JsonProcessingException e) {
                throw new BadRequestException("Invalid options format for question: " + dto.getText());
            }
            questions.add(q);
        }
        return questions;
    }

    /** Response dành cho PARTNER (kèm correct_answer) */
    private QuizTemplateResponseDto toDetailResponse(QuizTemplate t, boolean includeQuestions) {
        return QuizTemplateResponseDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .questionCount(t.getQuestions() != null ? t.getQuestions().size() : 0)
                .active(t.getActive())
                .partnerId(t.getPartner() != null ? t.getPartner().getId() : null)
                .partnerName(t.getPartner() != null ? t.getPartner().getOrganizationName() : null)
                .isCompetition(t.getIsCompetition())
                .questions(includeQuestions && t.getQuestions() != null ? t.getQuestions().stream()
                        .map(q -> QuizTemplateResponseDto.QuestionResponseDto.builder()
                                .id(q.getId())
                                .text(q.getText())
                                .options(parseOptions(q.getOptionsJson()))
                                .correctAnswer(q.getCorrectAnswer())
                                .explanation(q.getExplanation())
                                .build())
                        .collect(Collectors.toList()) : null)
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }

    /** Response dành cho STUDENT (ẩn correct_answer) */
    private QuizTemplateResponseDto toStudentResponse(QuizTemplate t) {
        return QuizTemplateResponseDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .questionCount(t.getQuestions() != null ? t.getQuestions().size() : 0)
                .active(t.getActive())
                .partnerId(t.getPartner() != null ? t.getPartner().getId() : null)
                .partnerName(t.getPartner() != null ? t.getPartner().getOrganizationName() : null)
                .questions(t.getQuestions() != null ? t.getQuestions().stream()
                        .map(q -> QuizTemplateResponseDto.QuestionResponseDto.builder()
                                .id(q.getId())
                                .text(q.getText())
                                .options(parseOptions(q.getOptionsJson()))
                                .correctAnswer(null)  // ẨN đáp án đúng với student
                                .explanation(null)
                                .build())
                        .collect(Collectors.toList()) : null)
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }

    private QuizAttemptResponseDto toAttemptResponse(QuizAttempt a) {
        List<QuizAttemptResponseDto.PlacementDto> placements = a.getQuizPlacements() != null
                ? a.getQuizPlacements().stream().map(p -> QuizAttemptResponseDto.PlacementDto.builder()
                        .questionId(p.getQuestion().getId())
                        .questionText(p.getQuestion().getText())
                        .selectedAnswer(p.getSelectedAnswer())
                        .correctAnswer(p.getQuestion().getCorrectAnswer())
                        .isCorrect(p.getIsCorrect())
                        .build())
                .collect(Collectors.toList())
                : List.of();

        return QuizAttemptResponseDto.builder()
                .attemptId(a.getId())
                .quizTemplateId(a.getQuizTemplate().getId())
                .quizTitle(a.getQuizTemplate().getTitle())
                .studentId(a.getStudent().getId())
                .studentName(a.getStudent().getUser() != null ? a.getStudent().getUser().getFullName() : null)
                .score(a.getScore())
                .correctAmount(a.getCorrectAmount())
                .wrongAmount(a.getWrongAmount())
                .totalQuestions(a.getQuizTemplate().getQuestions() != null ? a.getQuizTemplate().getQuestions().size() : 0)
                .duration(a.getDuration())
                .attemptNumber(a.getAttemptNumber())
                .completed(a.getCompleted())
                .placements(placements)
                .createdAt(a.getCreatedAt())
                .build();
    }

    public void handleQuizResult(QuizAttempt attempt, int point) {
        if (point == 0) return;
        Student student = attempt.getStudent();

        // SCHOOL
        leaderboardService.upsertLeaderboardEntry(
                student.getId(),
                student.getPartner().getId(),
                LeaderboardScope.valueOf("SCHOOL"),
                student.getGrade(),
                point
        );

        // CLASS
        leaderboardService.upsertLeaderboardEntry(
                student.getId(),
                student.getPartner().getId(),
                LeaderboardScope.valueOf("CLASS"),
                student.getGrade(),
                point
        );
    }

    private List<String> parseOptions(String json) {
        if (json == null) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }
}
