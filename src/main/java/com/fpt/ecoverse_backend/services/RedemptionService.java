package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.responses.RedemptionResponseDto;

import java.util.List;

public interface RedemptionService {
    RedemptionResponseDto createRedemption(String studentId, String rewardItemId);
    RedemptionResponseDto approveRedemptionByParent(String parentId, String redemptionId, boolean approved, String parentReason);
    RedemptionResponseDto approveRedemptionByPartner(String partnerId, String redemptionId, boolean approved, String partnerReason);
    RedemptionResponseDto fulfillRedemption(String redemptionId, String partnerId);
    List<RedemptionResponseDto> getRedemptionRequests(String userId);
}
