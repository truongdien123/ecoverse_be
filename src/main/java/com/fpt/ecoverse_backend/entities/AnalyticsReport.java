package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsReport extends BaseEntity {

    @Column(name = "total_users")
    private Integer totalUsers;

    @Column(name = "total_points_distributed")
    private Integer totalPointsDistributed;

    @Column(name = "redemption_rate", precision = 5, scale = 2)
    private BigDecimal redemptionRate;

    @Column(name = "engagement_rate", precision = 5, scale = 2)
    private BigDecimal engagementRate;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}
