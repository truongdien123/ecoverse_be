package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.responses.RedemptionResponseDto;
import com.fpt.ecoverse_backend.services.RedemptionService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/redemptions")
public class RedemptionController {

    private final RedemptionService redemptionService;

    public RedemptionController(RedemptionService redemptionService) {
        this.redemptionService = redemptionService;
    }

    @PostMapping("/students/{student_id}/reward-items/{rewardItem_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createRedemption(@PathVariable("student_id") String studentId, @PathVariable("rewardItem_id") String rewardItemId) {
        RedemptionResponseDto response = redemptionService.createRedemption(studentId, rewardItemId);
        return ResponseUtil.success("Redemption request created successfully", response);
    }

    @PostMapping("/{redemption_id}/parents/{parent_id}/approve")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> approveByParent(
            @PathVariable("redemption_id") String redemptionId,
            @PathVariable("parent_id") String parentId,
            @RequestParam("approved") boolean approved,
            @RequestParam(value = "reason", required = false) String reason) {

        RedemptionResponseDto response =
                redemptionService.approveRedemptionByParent(parentId, redemptionId, approved, reason);

        return ResponseUtil.success("Parent approval processed", response);
    }

    @PostMapping("/{redemption_id}/partners/{partner_id}/approve")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> approveByPartner(
            @PathVariable("redemption_id") String redemptionId,
            @PathVariable("partner_id") String partnerId,
            @RequestParam("approved") boolean approved,
            @RequestParam(value = "reason", required = false) String reason) {

        RedemptionResponseDto response =
                redemptionService.approveRedemptionByPartner(partnerId, redemptionId, approved, reason);

        return ResponseUtil.success("Partner approval processed", response);
    }

    @PostMapping("/{redemption_id}/partners/{partner_id}/fulfill")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> fulfillRedemption(
            @PathVariable("redemption_id") String redemptionId,
            @PathVariable("partner_id") String partnerId) {

        RedemptionResponseDto response =
                redemptionService.fulfillRedemption(redemptionId, partnerId);

        return ResponseUtil.success("Redemption fulfilled successfully", response);
    }

    @GetMapping("/users/{user_id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT', 'PARTNERSHIP')")
    public ResponseEntity<?> getRedemptionsByUser(
            @PathVariable("user_id") String userId) {

        List<RedemptionResponseDto> responses =
                redemptionService.getRedemptionRequests(userId);

        return ResponseUtil.success("Get redemption requests successfully", responses);
    }

    @PostMapping("/parents/{parent_id}/students/{student_id}/reward-items/{rewardItem_id}")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> createRedemptionByParent(@PathVariable("parent_id") String parentId, @PathVariable("student_id") String studentId, @PathVariable("rewardItem_id") String rewardItemId) {
        return ResponseUtil.success("Create redemption request successfully", redemptionService.createRedemptionByParent(parentId, studentId, rewardItemId));
    }

}
