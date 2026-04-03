package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;

import java.util.List;

public interface AdminService {
    PartnerResponseDto verifyPartner(String adminId, String partnerId, Boolean isApproved);
    List<PartnerResponseDto> getListPartner(String adminId, String status, PageFilterRequestDto pageFilterRequestDto);
}
