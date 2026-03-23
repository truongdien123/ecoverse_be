package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "game_attempts", indexes = {
        @Index(name = "idx_game_attempts_round", columnList = "game_round_id"),
        @Index(name = "idx_game_attempts_student", columnList = "student_id"),
        @Index(name = "idx_game_attempts_round_student", columnList = "game_round_id,student_id"),
        @Index(name = "idx_game_attempts_completed", columnList = "completed")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}