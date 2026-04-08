package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.RewardItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.RewardItemResponseDto;
import com.fpt.ecoverse_backend.services.RewardService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @PostMapping(value = "/{partner_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> createReward(@PathVariable("partner_id") String partnerId, @ModelAttribute RewardItemRequestDto request) {
        RewardItemResponseDto responseDto = rewardService.createRewardItem(partnerId, request);
        return ResponseUtil.success("Reward item created successfully", responseDto);
    }

    @PostMapping("/{partner_id}/get-list")
    @PreAuthorize("hasAnyRole('PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getRewards(@PathVariable("partner_id") String partnerId, @RequestBody PageFilterRequestDto pageFilterRequestDto) {
        List<RewardItemResponseDto> response = rewardService.getRewardItems(partnerId, pageFilterRequestDto);
        return ResponseUtil.success("Reward items retrieved successfully", response);
    }

    @GetMapping("/items/{reward_item_id}/partners/{partner_id}")
    @PreAuthorize("hasAnyRole('PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getRewardItemById(@PathVariable("partner_id") String partnerId, @PathVariable("reward_item_id") String rewardItemId) {
        RewardItemResponseDto response = rewardService.getRewardItemById(partnerId, rewardItemId);
        return ResponseUtil.success("Reward item retrieved successfully", response);
    }

    @PutMapping(value = "/items/{reward_item_id}/partners/{partner_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> updateRewardItem(@PathVariable("partner_id") String partnerId, @PathVariable("reward_item_id") String rewardItemId, @ModelAttribute RewardItemRequestDto request) {
        RewardItemResponseDto response = rewardService.updateRewardItem(partnerId, rewardItemId, request);
        return ResponseUtil.success("Reward item updated successfully", response);
    }

    @DeleteMapping("/items/{reward_item_id}/partners/{partner_id}")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> deleteRewardItem(@PathVariable("partner_id") String partnerId, @PathVariable("reward_item_id") String rewardItemId) {
        RewardItemResponseDto response = rewardService.deleteRewardItem(partnerId, rewardItemId);
        return ResponseUtil.success("Reward item deleted successfully", response);
    }
}
