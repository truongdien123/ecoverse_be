package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "grade", length = 10)
    private String grade;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    @Column(name = "student_code", length = 50, unique = true)
    private String studentCode;

    @Column(name = "points", columnDefinition = "integer default 0")
    private Integer points = 0;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAttempt> quizAttempts;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAchievement> studentAchievements;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameAttempt> gameAttempts;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaderboardEntry> leaderboardEntries;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionParticipant> competitionParticipants;
}
