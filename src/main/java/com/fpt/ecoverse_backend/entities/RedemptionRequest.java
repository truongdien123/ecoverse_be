package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "redemption_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionRequest extends BaseEntity {

    @Column(name = "reason_parent", length = 36)
    private String reasonParent;

    @Column(name = "reason_partner", length = 36)
    private String reasonPartner;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_approval", length = 50, columnDefinition = "varchar(50) default 'PENDING'")
    private ApprovalStatus parentApproval = ApprovalStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "partner_approval", length = 50, columnDefinition = "varchar(50) default 'PENDING'")
    private ApprovalStatus partnerApproval = ApprovalStatus.PENDING;

    @Column(name = "fulfilled", nullable = false, columnDefinition = "boolean default false")
    private Boolean fulfilled = false;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne
    @JoinColumn(name = "reward_item_id")
    private RewardItem rewardItem;

}