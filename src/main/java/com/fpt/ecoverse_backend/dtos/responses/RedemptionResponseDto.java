package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionResponseDto {

    @JsonProperty("redemption_id")
    private String redemptionId;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("reward_item_id")
    private String rewardItemId;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("redemption_date")
    private String redemptionDate;

    @JsonProperty("parent_approved")
    private Boolean parentApproved;

    @JsonProperty("partner_approved")
    private Boolean partnerApproved;

    @JsonProperty("fulfilled")
    private Boolean fulfilled;
}
