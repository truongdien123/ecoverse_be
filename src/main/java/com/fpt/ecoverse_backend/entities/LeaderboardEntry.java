package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.LeaderboardScope;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leaderboard_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardEntry extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 50)
    private LeaderboardScope scope;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "grade", length = 10)
    private String grade;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}
