package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.StatisticPartner;
import com.fpt.ecoverse_backend.dtos.StatisticStudent;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.projections.AccuracyStatsProjection;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.StatisticService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatisticServiceImp implements StatisticService {

    private final GameAttemptRepository gameAttemptRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final StudentRepository studentRepository;
    private final PartnerRepository partnerRepository;
    private final ParentRepository parentRepository;
    private final GameRoundRepository gameRoundRepository;
    private final QuizTemplateRepository quizTemplateRepository;
    private final RedemptionRequestRepository redemptionRequestRepository;

    public StatisticServiceImp(GameAttemptRepository gameAttemptRepository,
                               QuizAttemptRepository quizAttemptRepository,
                               StudentRepository studentRepository, PartnerRepository partnerRepository, ParentRepository parentRepository, GameRoundRepository gameRoundRepository, QuizTemplateRepository quizTemplateRepository, RedemptionRequestRepository redemptionRequestRepository) {
        this.gameAttemptRepository = gameAttemptRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.studentRepository = studentRepository;
        this.partnerRepository = partnerRepository;
        this.parentRepository = parentRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.quizTemplateRepository = quizTemplateRepository;
        this.redemptionRequestRepository = redemptionRequestRepository;
    }

    @Override
    public StatisticStudent getStudentStatistic(String studentId) {

        // ===== 1. TOTAL GAMES PLAYED =====
        int totalGamesPlayed = gameAttemptRepository.findDistinctByGameRound(studentId).size();

        // ===== 2. TOTAL QUIZ COMPLETED =====
        int totalQuizzesCompleted = quizAttemptRepository.findDistinctByQuizTemplate(studentId).size();

        // ===== 3. TOTAL ACHIEVEMENTS =====
        int totalAchievementsUnlocked = studentRepository.countAchievementsUnlocked(studentId);

        // ===== 4. AVERAGE ACCURACY =====
        AccuracyStatsProjection stats = studentRepository.getAverageAccuracyStats(studentId);
        double avgAccuracy = (stats != null) ? stats.getAccuracyPercentage() : 0;

        // ===== BUILD DTO =====
        return new StatisticStudent(
                totalGamesPlayed,
                avgAccuracy,
                totalQuizzesCompleted,
                totalAchievementsUnlocked
        );
    }

    @Override
    public StatisticPartner getPartnerStatistic(String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        long totalStudent = studentRepository.countStudentByPartnerId(partnerId);
        long totalParent = parentRepository.countParentsByPartnerId(partnerId);
        long totalActiveGame = gameRoundRepository.countByPartnerId(partnerId);
        long totalActiveQuiz = quizTemplateRepository.countByPartnerId(partnerId);
        long totalPointsDistributed = getTotalPointsDistributed(partnerId);
        long totalRedemptionRequest = redemptionRequestRepository.countByPartnerId(partnerId);
        return new StatisticPartner(
                totalStudent,
                totalParent,
                totalActiveGame,
                totalActiveQuiz,
                totalPointsDistributed,
                totalRedemptionRequest
        );
    }

    private long getTotalPointsDistributed(String partnerId) {

        Long gamePoints = gameAttemptRepository.sumGamePoints(partnerId);
        Long quizPoints = quizAttemptRepository.sumQuizPoints(partnerId);

        return (gamePoints == null ? 0 : gamePoints)
                + (quizPoints == null ? 0 : quizPoints);
    }
}
