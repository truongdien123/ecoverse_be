package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.responses.dashboard.ActivityStatisticsResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.dashboard.PartnerPointResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.dashboard.UserOverviewResponseDto;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImp implements DashboardService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PartnerRepository partnerRepository;
    private final GameAttemptRepository gameAttemptRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final RedemptionRequestRepository redemptionRequestRepository;

    @Override
    public UserOverviewResponseDto getUserOverview() {
        return new UserOverviewResponseDto(
                studentRepository.count(),
                parentRepository.count(),
                partnerRepository.count()
        );
    }

    @Override
    public ActivityStatisticsResponseDto getActivityStatistics() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return new ActivityStatisticsResponseDto(
                gameAttemptRepository.countByCreatedAtAfter(yesterday),
                quizAttemptRepository.countByCreatedAtAfter(yesterday)
        );
    }

    @Override
    public Map<ApprovalStatus, Long> getRedemptionStatistics() {
        List<Object[]> rawStats = redemptionRequestRepository.countRedemptionByStatus();
        return rawStats.stream()
                .collect(Collectors.toMap(
                        row -> (ApprovalStatus) row[0],
                        row -> (Long) row[1]
                ));
    }

    @Override
    public long getPartnerCount() {
        return partnerRepository.count();
    }

    @Override
    public List<PartnerPointResponseDto> getPartnerPoints() {
        return partnerRepository.getPointsSummary();
    }
}
