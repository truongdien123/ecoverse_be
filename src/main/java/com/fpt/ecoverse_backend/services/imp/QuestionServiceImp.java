package com.fpt.ecoverse_backend.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoverse_backend.dtos.requests.QuestionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizTemplateResponseDto;
import com.fpt.ecoverse_backend.entities.Question;
import com.fpt.ecoverse_backend.entities.QuizTemplate;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.repositories.QuestionRepository;
import com.fpt.ecoverse_backend.repositories.QuizTemplateRepository;
import com.fpt.ecoverse_backend.services.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImp implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizTemplateRepository quizTemplateRepository;
    private final ObjectMapper objectMapper;

    public QuestionServiceImp(QuestionRepository questionRepository, QuizTemplateRepository quizTemplateRepository, ObjectMapper objectMapper) {
        this.questionRepository = questionRepository;
        this.quizTemplateRepository = quizTemplateRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<QuizTemplateResponseDto.QuestionResponseDto> getQuestionsByQuizId(String partnerId, String quizId) {
        QuizTemplate template = quizTemplateRepository.findByIdAndPartnerId(quizId, partnerId)
                .orElseThrow(() -> new NotFoundException("Quiz template not found or access denied"));

        return template.getQuestions().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuizTemplateResponseDto.QuestionResponseDto addQuestionToQuiz(String partnerId, String quizId, QuestionRequestDto request) {
        QuizTemplate template = quizTemplateRepository.findByIdAndPartnerId(quizId, partnerId)
                .orElseThrow(() -> new NotFoundException("Quiz template not found or access denied"));

        Question question = new Question();
        question.setText(request.getText());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setExplanation(request.getExplanation());
        question.setQuizTemplate(template);

        try {
            question.setOptionsJson(objectMapper.writeValueAsString(request.getOptions()));
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid options format");
        }

        Question saved = questionRepository.save(question);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public List<QuizTemplateResponseDto.QuestionResponseDto> addMultipleQuestionsToQuiz(String partnerId, String quizId, List<QuestionRequestDto> requests) {
        QuizTemplate template = quizTemplateRepository.findByIdAndPartnerId(quizId, partnerId)
                .orElseThrow(() -> new NotFoundException("Quiz template not found or access denied"));

        List<Question> questions = requests.stream().map(req -> {
            Question question = new Question();
            question.setText(req.getText());
            question.setCorrectAnswer(req.getCorrectAnswer());
            question.setExplanation(req.getExplanation());
            question.setQuizTemplate(template);
            try {
                question.setOptionsJson(objectMapper.writeValueAsString(req.getOptions()));
            } catch (JsonProcessingException e) {
                throw new BadRequestException("Invalid options format for question: " + req.getText());
            }
            return question;
        }).collect(Collectors.toList());

        List<Question> savedQuestions = questionRepository.saveAll(questions);
        return savedQuestions.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuizTemplateResponseDto.QuestionResponseDto updateQuestion(String partnerId, String questionId, QuestionRequestDto request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));

        if (!question.getQuizTemplate().getPartner().getId().equals(partnerId)) {
            throw new NotFoundException("Question not found or access denied");
        }

        question.setText(request.getText());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setExplanation(request.getExplanation());

        try {
            question.setOptionsJson(objectMapper.writeValueAsString(request.getOptions()));
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid options format");
        }

        Question saved = questionRepository.save(question);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteQuestion(String partnerId, String questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));

        if (!question.getQuizTemplate().getPartner().getId().equals(partnerId)) {
            throw new NotFoundException("Question not found or access denied");
        }

        // Instead of soft delete, questions might be physically deleted if we choose,
        // but considering earlier concerns with deleting history, we should either ensure no attempts exist,
        // or accept cascading deletion. Since the user wanted clear CRUD:
        questionRepository.delete(question);
    }

    private QuizTemplateResponseDto.QuestionResponseDto toResponse(Question q) {
        return QuizTemplateResponseDto.QuestionResponseDto.builder()
                .id(q.getId())
                .text(q.getText())
                .options(parseOptions(q.getOptionsJson()))
                .correctAnswer(q.getCorrectAnswer())
                .explanation(q.getExplanation())
                .build();
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
