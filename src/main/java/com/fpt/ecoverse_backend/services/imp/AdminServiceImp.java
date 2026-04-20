package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.PartnerStatusChangedEvent;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import com.fpt.ecoverse_backend.enums.PartnerStatus;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.PartnerMapper;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.services.AdminService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final ApplicationEventPublisher eventPublisher;

    public AdminServiceImp(UserRepository userRepository, PartnerRepository partnerRepository, PartnerMapper partnerMapper, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
        this.eventPublisher = eventPublisher;
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
        eventPublisher.publishEvent(new PartnerStatusChangedEvent(partnerId, partner.getUser().getEmail(), partner.getStatus().name()));
        return partnerMapper.toPartnerResponse(partner, partner.getUser());
    }

    @Override
    public List<PartnerResponseDto> getListPartner(String adminId, String status, PageFilterRequestDto pageFilterRequestDto) {
        Optional<User> adminOpt = userRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new NotFoundException("Admin not found");
        } else if (adminOpt.get().getRole() != UserType.ADMIN) {
            throw new BadRequestException("User is not an admin");
        }
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize());
        PartnerStatus statusEnum = null;

        if (!status.equalsIgnoreCase("all")) {
            statusEnum = PartnerStatus.valueOf(status.toUpperCase());
        }
        Page<Partner> partnerPage = partnerRepository.findPartnersByStatus(status.equalsIgnoreCase("all") ? null : statusEnum, pageable);
        List<Partner> partners = partnerPage.getContent();
        return partners.stream()
                .map(partner -> partnerMapper.toPartnerResponse(partner, partner.getUser()))
                .toList();
    }
}
