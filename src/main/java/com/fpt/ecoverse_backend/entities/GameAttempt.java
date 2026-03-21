package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game_attempts", indexes = {
    @Index(name = "idx_game_attempts_round", columnList = "game_round_id"),
    @Index(name = "idx_game_attempts_student", columnList = "student_id"),
    @Index(name = "idx_game_attempts_round_student", columnList = "game_round_id,student_id"),
    @Index(name = "idx_game_attempts_completed", columnList = "completed")
})
public class GameAttempt extends BaseEntity {

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "points_earned", columnDefinition = "integer default 0")
    private Integer pointsEarned = 0;

    @Column(name = "correct_count", columnDefinition = "integer default 0")
    private Integer correctCount = 0;

    @Column(name = "total_items", nullable = false)
    private Integer totalItems;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Column(name = "completed", nullable = false, columnDefinition = "boolean default false")
    private Boolean completed = false;

    @ManyToOne
    @JoinColumn(name = "game_round_id")
    private GameRound gameRound;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "gameAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamePlacement> gamePlacements;

    public GameAttempt() {
    }

    public GameAttempt(Integer duration, Integer pointsEarned, Integer correctCount, Integer totalItems, Integer attemptNumber, Boolean completed, GameRound gameRound, Student student, List<GamePlacement> gamePlacements) {
        this.duration = duration;
        this.pointsEarned = pointsEarned;
        this.correctCount = correctCount;
        this.totalItems = totalItems;
        this.attemptNumber = attemptNumber;
        this.completed = completed;
        this.gameRound = gameRound;
        this.student = student;
        this.gamePlacements = gamePlacements;
    }

    public GameAttempt(String id, LocalDateTime createdAt, LocalDateTime updatedAt, Integer duration, Integer pointsEarned, Integer correctCount, Integer totalItems, Integer attemptNumber, Boolean completed, GameRound gameRound, Student student, List<GamePlacement> gamePlacements) {
        super(id, createdAt, updatedAt);
        this.duration = duration;
        this.pointsEarned = pointsEarned;
        this.correctCount = correctCount;
        this.totalItems = totalItems;
        this.attemptNumber = attemptNumber;
        this.completed = completed;
        this.gameRound = gameRound;
        this.student = student;
        this.gamePlacements = gamePlacements;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public GameRound getGameRound() {
        return gameRound;
    }

    public void setGameRound(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<GamePlacement> getGamePlacements() {
        return gamePlacements;
    }

    public void setGamePlacements(List<GamePlacement> gamePlacements) {
        this.gamePlacements = gamePlacements;
    }
}
