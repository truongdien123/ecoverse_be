package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.QuestionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.QuizTemplateResponseDto;

import java.util.List;

public interface QuestionService {
    List<QuizTemplateResponseDto.QuestionResponseDto> getQuestionsByQuizId(String partnerId, String quizId);
    QuizTemplateResponseDto.QuestionResponseDto addQuestionToQuiz(String partnerId, QuestionRequestDto request);
    List<QuizTemplateResponseDto.QuestionResponseDto> addMultipleQuestionsToQuiz(String partnerId, String quizId, List<QuestionRequestDto> requests);
    List<QuizTemplateResponseDto.QuestionResponseDto> getQuestionBank(String partnerId);
    List<QuizTemplateResponseDto.QuestionResponseDto> importQuestionsFromBank(String partnerId, String quizId, List<String> questionIds);
    QuizTemplateResponseDto.QuestionResponseDto updateQuestion(String partnerId, String questionId, QuestionRequestDto request);
    void deleteQuestion(String partnerId, String questionId);
}
