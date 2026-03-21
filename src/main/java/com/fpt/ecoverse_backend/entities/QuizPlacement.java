package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_placements", indexes = {
    @Index(name = "idx_quiz_placements_attempt", columnList = "quiz_attempt_id"),
    @Index(name = "idx_quiz_placements_question", columnList = "question_id"),
    @Index(name = "idx_quiz_placements_attempt_question", columnList = "quiz_attempt_id,question_id", unique = true)
})
public class QuizPlacement extends BaseEntity {

    @Column(name = "selected_answer", length = 255)
    private String selectedAnswer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public QuizPlacement() {
    }

    public QuizPlacement(String selectedAnswer, Boolean isCorrect, QuizAttempt quizAttempt, Question question) {
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.quizAttempt = quizAttempt;
        this.question = question;
    }

    public QuizPlacement(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String selectedAnswer, Boolean isCorrect, QuizAttempt quizAttempt, Question question) {
        super(id, createdAt, updatedAt);
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.quizAttempt = quizAttempt;
        this.question = question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
