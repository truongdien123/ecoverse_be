package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAchievement extends BaseEntity {

    @Column(name = "awarded_at")
    private LocalDateTime awardedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "achievement_id")
    private Achievement achievement;
}