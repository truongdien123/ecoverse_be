package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.RewardItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.RewardItemResponseDto;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.RewardItem;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.RewardItemMapper;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.RewardItemRepository;
import com.fpt.ecoverse_backend.services.RewardService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RewardServiceImp implements RewardService {

    private final RewardItemRepository rewardItemRepository;
    private final RewardItemMapper rewardItemMapper;
    private final PartnerRepository partnerRepository;
    private final UploadFile uploadFile;

    public RewardServiceImp(RewardItemRepository rewardItemRepository, RewardItemMapper rewardItemMapper, PartnerRepository partnerRepository, UploadFile uploadFile) {
        this.rewardItemRepository = rewardItemRepository;
        this.rewardItemMapper = rewardItemMapper;
        this.partnerRepository = partnerRepository;
        this.uploadFile = uploadFile;
    }

    @Override
    public RewardItemResponseDto createRewardItem(String partnerId, RewardItemRequestDto request) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
        RewardItem rewardItem = rewardItemMapper.toRewardItem(request, null);
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            rewardItem.setImageUrl(uploadFile.imageToUrl(request.getImage()));
        }
        rewardItem.setPartner(partnerOpt.get());
        rewardItemRepository.save(rewardItem);
        RewardItemResponseDto response = rewardItemMapper.toRewardItemResponse(rewardItem);
        response.setPartnerId(partnerId);
        return response;
    }

    @Override
    public List<RewardItemResponseDto> getRewardItems(String partnerId, PageFilterRequestDto pageFilterRequestDto) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize());
        Page<RewardItem> rewardItemsPage = rewardItemRepository.findRewardItems(partnerId, pageable);
        List<RewardItem> rewardItems = rewardItemsPage.getContent();
        List<RewardItemResponseDto> response = rewardItems.stream().map(rewardItemMapper::toRewardItemResponse).toList();
        response.forEach(r -> r.setPartnerId(partnerId));
        return response;
    }

    @Override
    public RewardItemResponseDto getRewardItemById(String partnerId, String rewardItemId) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
         Optional<RewardItem> rewardItemOpt = rewardItemRepository.findById(rewardItemId);
         if (rewardItemOpt.isEmpty() || !rewardItemOpt.get().getPartner().getId().equals(partnerId)) {
             throw new NotFoundException("Reward item not found");
         }
        RewardItemResponseDto response = rewardItemMapper.toRewardItemResponse(rewardItemOpt.get());
        response.setPartnerId(partnerId);
        return response;
    }

    @Override
    public RewardItemResponseDto updateRewardItem(String partnerId, String rewardItemId, RewardItemRequestDto request) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
        Optional<RewardItem> rewardItemOpt = rewardItemRepository.findById(rewardItemId);
        if (rewardItemOpt.isEmpty() || !rewardItemOpt.get().getPartner().getId().equals(partnerId)) {
            throw new NotFoundException("Reward item not found");
        }
        RewardItem rewardItem = rewardItemOpt.get();
        rewardItemMapper.updateRewardItem(rewardItem, request);
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            rewardItem.setImageUrl(uploadFile.imageToUrl(request.getImage()));
        }
        rewardItemRepository.save(rewardItem);
        RewardItemResponseDto response = rewardItemMapper.toRewardItemResponse(rewardItem);
        response.setPartnerId(partnerId);
        return response;
    }

    @Override
    public RewardItemResponseDto deleteRewardItem(String partnerId, String rewardItemId) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
        Optional<RewardItem> rewardItemOpt = rewardItemRepository.findById(rewardItemId);
        if (rewardItemOpt.isEmpty() || !rewardItemOpt.get().getPartner().getId().equals(partnerId)) {
            throw new NotFoundException("Reward item not found");
        }
        rewardItemRepository.delete(rewardItemOpt.get());
        return rewardItemMapper.toRewardItemResponse(rewardItemOpt.get());
    }
}
