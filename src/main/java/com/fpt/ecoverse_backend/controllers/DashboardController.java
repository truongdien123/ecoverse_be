package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.responses.dashboard.ActivityStatisticsResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.dashboard.PartnerPointResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.dashboard.UserOverviewResponseDto;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import com.fpt.ecoverse_backend.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Các API thống kê cho Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/users/overview")
    @Operation(summary = "Lấy tổng số học sinh, phụ huynh, đối tác")
    public ResponseEntity<UserOverviewResponseDto> getUserOverview() {
        return ResponseEntity.ok(dashboardService.getUserOverview());
    }

    @GetMapping("/activities/recent-24h")
    @Operation(summary = "Thống kê lượt chơi game và làm quiz trong 24h qua")
    public ResponseEntity<ActivityStatisticsResponseDto> getActivityStatistics() {
        return ResponseEntity.ok(dashboardService.getActivityStatistics());
    }

    @GetMapping("/redemptions/statistics")
    @Operation(summary = "Lấy tất cả trạng thái đổi thưởng hiện tại")
    public ResponseEntity<Map<ApprovalStatus, Long>> getRedemptionStatistics() {
        return ResponseEntity.ok(dashboardService.getRedemptionStatistics());
    }

    @GetMapping("/partners/count")
    @Operation(summary = "Lấy số lượng đối tác đang có")
    public ResponseEntity<Long> getPartnerCount() {
        return ResponseEntity.ok(dashboardService.getPartnerCount());
    }

    @GetMapping("/partners/points")
    @Operation(summary = "Lấy tổng số điểm của từng đối tác")
    public ResponseEntity<List<PartnerPointResponseDto>> getPartnerPoints() {
        return ResponseEntity.ok(dashboardService.getPartnerPoints());
    }
}
