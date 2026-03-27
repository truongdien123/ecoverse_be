package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.CompetitionStatus;
import com.fpt.ecoverse_backend.enums.TargetScope;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "competitions", indexes = {
        @Index(name = "idx_competitions_partner", columnList = "partner_id"),
        @Index(name = "idx_competitions_status", columnList = "status"),
        @Index(name = "idx_competitions_start_time", columnList = "start_time")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competition extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, columnDefinition = "varchar(50) default 'DRAFT'")
    private CompetitionStatus status = CompetitionStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_scope", length = 50, columnDefinition = "varchar(50) default 'SCHOOL'")
    private TargetScope targetScope = TargetScope.SCHOOL;

    @Column(name = "target_class_code")
    private String targetClassCode;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionLink> competitionLinks;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionParticipant> competitionParticipants;
}