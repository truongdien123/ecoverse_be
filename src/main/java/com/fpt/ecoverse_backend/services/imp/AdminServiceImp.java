package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.enums.PartnerStatus;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.PartnerMapper;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.services.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;

    public AdminServiceImp(UserRepository userRepository, PartnerRepository partnerRepository, PartnerMapper partnerMapper) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
    }


    @Override
    public PartnerResponseDto verifyPartner(String adminId, String partnerId, Boolean isApproved) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin not found"));
        if (admin.getRole() != UserType.ADMIN) {
            throw new BadRequestException("User is not an admin");
        }
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new NotFoundException("Partner not found"));
        if (partner.getStatus() == PartnerStatus.APPROVED) {
            throw new BadRequestException("Partner has already been approved");
        }
        partner.setStatus(isApproved ? PartnerStatus.APPROVED : PartnerStatus.REJECTED);
        partnerRepository.save(partner);
        return partnerMapper.toPartnerResponse(partner, partner.getUser());
    }
}
