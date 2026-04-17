package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.responses.dashboard.ActivityStatisticsResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.dashboard.PartnerPointResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.dashboard.UserOverviewResponseDto;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    UserOverviewResponseDto getUserOverview();
    ActivityStatisticsResponseDto getActivityStatistics();
    Map<ApprovalStatus, Long> getRedemptionStatistics();
    long getPartnerCount();
    List<PartnerPointResponseDto> getPartnerPoints();
}
