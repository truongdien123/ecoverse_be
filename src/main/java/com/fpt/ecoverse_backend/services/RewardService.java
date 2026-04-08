package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.RewardItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.RewardItemResponseDto;

import java.util.List;

public interface RewardService {
    RewardItemResponseDto createRewardItem(String partnerId, RewardItemRequestDto request);
    List<RewardItemResponseDto> getRewardItems(String partnerId, PageFilterRequestDto pageFilterRequestDto);
    List<RewardItemResponseDto> getAllRewardItems();
    RewardItemResponseDto getRewardItemById(String partnerId, String rewardItemId);
    RewardItemResponseDto updateRewardItem(String partnerId, String rewardItemId, RewardItemRequestDto request);
    RewardItemResponseDto deleteRewardItem(String partnerId, String rewardItemId);
    
}
