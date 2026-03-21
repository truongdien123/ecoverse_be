package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.PartnerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partner extends BaseEntity {

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "organization_name", nullable = false, length = 255)
    private String organizationName;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "contact_person", length = 255)
    private String contactPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, columnDefinition = "varchar(50) default 'PENDING'")
    private PartnerStatus status = PartnerStatus.PENDING;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parent> parents;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizTemplate> quizTemplates;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RewardItem> rewardItems;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaderboardEntry> leaderboardEntries;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnalyticsReport> analyticsReports;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameRound> gameRounds;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competition> competitions;
}
