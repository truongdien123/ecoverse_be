package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.responses.LeaderboardResponseDto;
import com.fpt.ecoverse_backend.entities.LeaderboardEntry;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.enums.LeaderboardScope;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.projections.LeaderboardProjection;
import com.fpt.ecoverse_backend.repositories.GameAttemptRepository;
import com.fpt.ecoverse_backend.repositories.LeaderboardEntryRepository;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.services.LeaderboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LeaderboardServiceImp implements LeaderboardService {

    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final StudentRepository studentRepository;
    private final PartnerRepository partnerRepository;
    private final GameAttemptRepository gameAttemptRepository;

    public LeaderboardServiceImp(LeaderboardEntryRepository leaderboardEntryRepository, StudentRepository studentRepository, PartnerRepository partnerRepository, GameAttemptRepository gameAttemptRepository) {
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.studentRepository = studentRepository;
        this.partnerRepository = partnerRepository;
        this.gameAttemptRepository = gameAttemptRepository;
    }


    @Override
    @Transactional
    public void upsertLeaderboardEntry(String studentId, String partnerId, LeaderboardScope scope, String grade, int pointsEarned) {
        Optional<LeaderboardEntry> entryOpt =
                leaderboardEntryRepository.findByStudentIdAndScopeAndPartnerIdAndGrade(
                        studentId, scope, partnerId, grade
                );

        if (entryOpt.isPresent()) {
            LeaderboardEntry entry = entryOpt.get();
            entry.setPoints(entry.getPoints() + pointsEarned);

            leaderboardEntryRepository.save(entry);

        } else {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setStudent(studentRepository.findById(studentId).get());
            entry.setPartner(partnerRepository.findById(partnerId).get());
            entry.setScope(scope);
            entry.setGrade(grade);
            entry.setPoints(pointsEarned);
            entry.setRank(0);
            entry.setCreatedAt(LocalDateTime.now());
            entry.setUpdatedAt(LocalDateTime.now());

            leaderboardEntryRepository.save(entry);
        }
    }

    @Override
    public List<LeaderboardResponseDto> getListLeaderboard(String partnerId, LeaderboardScope scope, String grade, int page, int size) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        Pageable pageable = PageRequest.of(page-1, size);
        Page<LeaderboardProjection> leaderboardEntries = leaderboardEntryRepository.getLeaderboard(partnerId, scope.name(), grade, pageable);
        return leaderboardEntries.getContent().stream().map(item -> {
            LeaderboardResponseDto dto = new LeaderboardResponseDto();
            dto.setStudentId(item.getStudentId());
            dto.setStudentName(item.getStudentName());
            dto.setGrade(item.getGrade());
            dto.setPoints(item.getPoints());
            dto.setMinDuration(item.getMinDuration());
            return dto;
        }).toList();
    }

    @Override
    public Long getStudentRank(String studentId, String partnerId, LeaderboardScope scope, String grade) {
        if (!partnerRepository.existsById(partnerId)) {
            throw new NotFoundException("Not found partner");
        }

        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException("Student not found");
        }

        Integer points = leaderboardEntryRepository.getStudentPoints(studentId, partnerId, scope, grade);
        if (points == null) return 0L;
        Long totalDuration = gameAttemptRepository.getTotalDurationByStudent(studentId);

        return leaderboardEntryRepository.getStudentRank(
                partnerId,
                scope,
                grade,
                points,
                totalDuration
        );
    }

}
