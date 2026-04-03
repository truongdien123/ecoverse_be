package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;

public interface AdminService {
    PartnerResponseDto verifyPartner(String adminId, String partnerId, Boolean isApproved);

}
