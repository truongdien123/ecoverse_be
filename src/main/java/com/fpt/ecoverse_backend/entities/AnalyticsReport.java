package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_reports")
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

    public AnalyticsReport() {
    }

    public AnalyticsReport(Integer totalUsers, Integer totalPointsDistributed, BigDecimal redemptionRate, BigDecimal engagementRate, Partner partner) {
        this.totalUsers = totalUsers;
        this.totalPointsDistributed = totalPointsDistributed;
        this.redemptionRate = redemptionRate;
        this.engagementRate = engagementRate;
        this.partner = partner;
    }

    public AnalyticsReport(String id, LocalDateTime createdAt, LocalDateTime updatedAt, Integer totalUsers, Integer totalPointsDistributed, BigDecimal redemptionRate, BigDecimal engagementRate, Partner partner) {
        super(id, createdAt, updatedAt);
        this.totalUsers = totalUsers;
        this.totalPointsDistributed = totalPointsDistributed;
        this.redemptionRate = redemptionRate;
        this.engagementRate = engagementRate;
        this.partner = partner;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Integer getTotalPointsDistributed() {
        return totalPointsDistributed;
    }

    public void setTotalPointsDistributed(Integer totalPointsDistributed) {
        this.totalPointsDistributed = totalPointsDistributed;
    }

    public BigDecimal getRedemptionRate() {
        return redemptionRate;
    }

    public void setRedemptionRate(BigDecimal redemptionRate) {
        this.redemptionRate = redemptionRate;
    }

    public BigDecimal getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(BigDecimal engagementRate) {
        this.engagementRate = engagementRate;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
