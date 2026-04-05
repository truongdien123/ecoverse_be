package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.RewardItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.RewardItemResponseDto;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.RewardItemMapper;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.RewardItemRepository;
import com.fpt.ecoverse_backend.services.RewardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RewardServiceImp implements RewardService {

    private final RewardItemRepository rewardItemRepository;
    private final RewardItemMapper rewardItemMapper;
    private final PartnerRepository partnerRepository;

    public RewardServiceImp(RewardItemRepository rewardItemRepository, RewardItemMapper rewardItemMapper, PartnerRepository partnerRepository) {
        this.rewardItemRepository = rewardItemRepository;
        this.rewardItemMapper = rewardItemMapper;
        this.partnerRepository = partnerRepository;
    }

    @Override
    public RewardItemResponseDto createRewardItem(String partnerId, RewardItemRequestDto request) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }

        return null;
    }

    @Override
    public List<RewardItemResponseDto> getRewardItems(String partnerId, PageFilterRequestDto pageFilterRequestDto) {
        return List.of();
    }

    @Override
    public RewardItemResponseDto getRewardItemById(String partnerId, String rewardItemId) {
        return null;
    }

    @Override
    public RewardItemResponseDto updateRewardItem(String partnerId, String rewardItemId, RewardItemRequestDto request) {
        return null;
    }

    @Override
    public RewardItemResponseDto deleteRewardItem(String partnerId, String rewardItemId) {
        return null;
    }
}
