package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "competition_participants", indexes = {
    @Index(name = "idx_competition_participants_competition", columnList = "competition_id"),
    @Index(name = "idx_competition_participants_student", columnList = "student_id"),
    @Index(name = "idx_competition_participants_unique", columnList = "competition_id,student_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionParticipant extends BaseEntity {

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "total_score", columnDefinition = "integer default 0")
    private Integer totalScore = 0;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
