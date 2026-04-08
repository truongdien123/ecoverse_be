package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.QuestionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuestionResponseDto;

import java.util.List;

public interface QuestionService {
    QuestionResponseDto createQuestion(QuestionRequestDto requestDto);
    QuestionResponseDto getQuestionById(String id);
    List<QuestionResponseDto> getQuestionsByQuizTemplateId(String quizTemplateId);
    QuestionResponseDto updateQuestion(String id, QuestionRequestDto requestDto);
    void deleteQuestion(String id);
}
