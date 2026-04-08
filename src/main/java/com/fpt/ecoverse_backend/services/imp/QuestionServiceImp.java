package com.fpt.ecoverse_backend.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoverse_backend.dtos.requests.QuestionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuestionResponseDto;
import com.fpt.ecoverse_backend.entities.Question;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.repositories.QuestionRepository;
import com.fpt.ecoverse_backend.repositories.QuizTemplateRepository;
import com.fpt.ecoverse_backend.services.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImp implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizTemplateRepository quizTemplateRepository;
    private final ObjectMapper objectMapper;

    public QuestionServiceImp(QuestionRepository questionRepository,
                              QuizTemplateRepository quizTemplateRepository,
                              ObjectMapper objectMapper) {
        this.questionRepository = questionRepository;
        this.quizTemplateRepository = quizTemplateRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public QuestionResponseDto createQuestion(QuestionRequestDto requestDto) {
        Question question = new Question();
        question.setText(requestDto.getText());
        question.setCorrectAnswer(requestDto.getCorrectAnswer());
        question.setExplanation(requestDto.getExplanation());
        question.setQuizTemplate(null); // Câu hỏi tạo trước, chưa gán vào bộ đề
        try {
            question.setOptionsJson(objectMapper.writeValueAsString(requestDto.getOptions()));
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Lỗi định dạng danh sách lựa chọn");
        }

        Question saved = questionRepository.save(question);
        return mapToResponseDto(saved);
    }

    @Override
    public QuestionResponseDto getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy câu hỏi với ID: " + id));
        return mapToResponseDto(question);
    }

    @Override
    public List<QuestionResponseDto> getQuestionsByQuizTemplateId(String quizTemplateId) {
        if (!quizTemplateRepository.existsById(quizTemplateId)) {
            throw new NotFoundException("Không tìm thấy bộ đề với ID: " + quizTemplateId);
        }
        return questionRepository.findByQuizTemplateId(quizTemplateId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionResponseDto updateQuestion(String id, QuestionRequestDto requestDto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy câu hỏi với ID: " + id));

        question.setText(requestDto.getText());
        question.setCorrectAnswer(requestDto.getCorrectAnswer());
        question.setExplanation(requestDto.getExplanation());
        try {
            question.setOptionsJson(objectMapper.writeValueAsString(requestDto.getOptions()));
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Lỗi định dạng danh sách lựa chọn");
        }

        Question updated = questionRepository.save(question);
        return mapToResponseDto(updated);
    }

    @Override
    public void deleteQuestion(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy câu hỏi với ID: " + id));
        questionRepository.delete(question);
    }

    private QuestionResponseDto mapToResponseDto(Question question) {
        return new QuestionResponseDto(
                question.getId(),
                question.getText(),
                question.getOptionsJson(),
                question.getCorrectAnswer(),
                question.getExplanation(),
                question.getQuizTemplate() != null ? question.getQuizTemplate().getId() : null,
                question.getCreatedAt(),
                question.getUpdatedAt()
        );
    }
}

