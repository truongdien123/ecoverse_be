package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.responses.RedemptionResponseDto;
import com.fpt.ecoverse_backend.repositories.RedemptionRequestRepository;
import com.fpt.ecoverse_backend.services.RedemptionService;
import org.springframework.stereotype.Service;

@Service
public class RedemptionServiceImp implements RedemptionService {

    private final RedemptionRequestRepository redemptionRequestRepository;

    public RedemptionServiceImp(RedemptionRequestRepository redemptionRequestRepository) {
        this.redemptionRequestRepository = redemptionRequestRepository;
    }

    @Override
    public RedemptionResponseDto createRedemption(String studentId, String rewardItemId) {
        return null;
    }

    @Override
    public RedemptionResponseDto approveRedemptionByParent(String parentId, String redemptionId, boolean approved) {
        return null;
    }

    @Override
    public RedemptionResponseDto approveRedemptionByPartner(String partnerId, String redemptionId, boolean approved) {
        return null;
    }

    @Override
    public RedemptionResponseDto fulfillRedemption(String redemptionId) {
        return null;
    }
}
