package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.responses.LeaderboardResponseDto;
import com.fpt.ecoverse_backend.entities.GameAttempt;
import com.fpt.ecoverse_backend.enums.LeaderboardScope;

import java.util.List;

public interface LeaderboardService {
    void upsertLeaderboardEntry(String studentId,
                           String partnerId,
                           LeaderboardScope scope,
                           String grade,
                           int pointsEarned);

    List<LeaderboardResponseDto> getListLeaderboard(String partnerId,
                                                    LeaderboardScope scope,
                                                    String grade,
                                                    int page,
                                                    int size);

    Long getStudentRank(
            String studentId,
            String partnerId,
            LeaderboardScope scope,
            String grade
    );
}
