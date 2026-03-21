package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_attempts", indexes = {
    @Index(name = "idx_quiz_attempts_template_id", columnList = "quiz_template_id"),
    @Index(name = "idx_quiz_attempts_student_id", columnList = "student_id"),
    @Index(name = "idx_quiz_attempts_template_student", columnList = "quiz_template_id,student_id"),
    @Index(name = "idx_quiz_attempts_completed", columnList = "completed")
})
public class QuizAttempt extends BaseEntity {

    @Column(name = "selected_answers_json", columnDefinition = "text")
    private String selectedAnswersJson;

    @Column(name = "score", columnDefinition = "integer default 0")
    private Integer score = 0;

    @Column(name = "correct_amount")
    private Integer correctAmount;

    @Column(name = "wrong_amount")
    private Integer wrongAmount;

    @Column(name = "completed", nullable = false, columnDefinition = "boolean default false")
    private Boolean completed = false;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "attempt_number", columnDefinition = "integer default 1")
    private Integer attemptNumber = 1;

    @ManyToOne
    @JoinColumn(name = "quiz_template_id")
    private QuizTemplate quizTemplate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizPlacement> quizPlacements;

    public QuizAttempt() {
    }

    public QuizAttempt(String selectedAnswersJson, Integer score, Integer correctAmount, Integer wrongAmount, Boolean completed, Integer duration, Integer attemptNumber, QuizTemplate quizTemplate, Student student, List<QuizPlacement> quizPlacements) {
        this.selectedAnswersJson = selectedAnswersJson;
        this.score = score;
        this.correctAmount = correctAmount;
        this.wrongAmount = wrongAmount;
        this.completed = completed;
        this.duration = duration;
        this.attemptNumber = attemptNumber;
        this.quizTemplate = quizTemplate;
        this.student = student;
        this.quizPlacements = quizPlacements;
    }

    public QuizAttempt(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String selectedAnswersJson, Integer score, Integer correctAmount, Integer wrongAmount, Boolean completed, Integer duration, Integer attemptNumber, QuizTemplate quizTemplate, Student student, List<QuizPlacement> quizPlacements) {
        super(id, createdAt, updatedAt);
        this.selectedAnswersJson = selectedAnswersJson;
        this.score = score;
        this.correctAmount = correctAmount;
        this.wrongAmount = wrongAmount;
        this.completed = completed;
        this.duration = duration;
        this.attemptNumber = attemptNumber;
        this.quizTemplate = quizTemplate;
        this.student = student;
        this.quizPlacements = quizPlacements;
    }

    public String getSelectedAnswersJson() {
        return selectedAnswersJson;
    }

    public void setSelectedAnswersJson(String selectedAnswersJson) {
        this.selectedAnswersJson = selectedAnswersJson;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCorrectAmount() {
        return correctAmount;
    }

    public void setCorrectAmount(Integer correctAmount) {
        this.correctAmount = correctAmount;
    }

    public Integer getWrongAmount() {
        return wrongAmount;
    }

    public void setWrongAmount(Integer wrongAmount) {
        this.wrongAmount = wrongAmount;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public QuizTemplate getQuizTemplate() {
        return quizTemplate;
    }

    public void setQuizTemplate(QuizTemplate quizTemplate) {
        this.quizTemplate = quizTemplate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<QuizPlacement> getQuizPlacements() {
        return quizPlacements;
    }

    public void setQuizPlacements(List<QuizPlacement> quizPlacements) {
        this.quizPlacements = quizPlacements;
    }
}
