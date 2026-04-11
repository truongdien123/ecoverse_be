package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.LeaderboardEntry;
import com.fpt.ecoverse_backend.enums.LeaderboardScope;
import com.fpt.ecoverse_backend.projections.LeaderboardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, String> {

    @Query("""
    SELECT le FROM LeaderboardEntry le
    WHERE le.student.id = :studentId
      AND le.scope = :scope
      AND le.partner.id = :partnerId
      AND (
            (:grade IS NULL AND le.grade IS NULL)
            OR le.grade = :grade
          )
""")
    Optional<LeaderboardEntry> findByStudentIdAndScopeAndPartnerIdAndGrade(
            @Param("studentId") String studentId,
            @Param("scope") LeaderboardScope scope,
            @Param("partnerId") String partnerId,
            @Param("grade") String grade
    );

    @Query("""
    SELECT
          le.student.id as studentId,
          u.fullName as studentName,
          s.grade as grade,
          le.points as points,
          COALESCE(MIN(ga.duration), 0) as minDuration
    FROM LeaderboardEntry le
    JOIN le.student s
    JOIN s.user u
    LEFT JOIN GameAttempt ga
        ON ga.student.id = s.id
        AND ga.completed = true
    WHERE le.partner.id = :partnerId
        AND le.scope = :scope
        AND (:grade IS NULL OR s.grade = :grade)
    GROUP BY le.student.id, u.fullName, s.grade, le.points
    ORDER BY
        le.points DESC,
        COALESCE(SUM(ga.duration), 0) ASC
""")
    Page<LeaderboardProjection> getLeaderboard(
            @Param("partnerId") String partnerId,
            @Param("scope") LeaderboardScope scope,
            @Param("grade") String grade,
            Pageable pageable
    );

    @Query("""
    SELECT le.points
    FROM LeaderboardEntry le
    WHERE le.student.id = :studentId
        AND le.partner.id = :partnerId
        AND le.scope = :scope
        AND (:grade IS NULL OR le.grade = :grade)
""")
    Integer getStudentPoints(@Param("studentId") String studentId,
                          @Param("partnerId") String partnerId,
                          @Param("scope") LeaderboardScope scope,
                          @Param("grade") String grade);

    @Query("""
    SELECT COUNT(le) + 1
    FROM LeaderboardEntry le
    WHERE le.partner.id = :partnerId
        AND le.scope = :scope
        AND (:grade IS NULL OR le.grade = :grade)
        AND (
            le.points > :points
            OR (le.points = :points AND
                (
                    SELECT COALESCE(SUM(ga.duration), 0)
                    FROM GameAttempt ga
                    WHERE ga.student.id = le.student.id
                        AND ga.completed = true
                ) < :totalDuration
            )
        )
""")
    Long getStudentRank(
            @Param("partnerId") String partnerId,
            @Param("scope") LeaderboardScope scope,
            @Param("grade") String grade,
            @Param("points") Integer points,
            @Param("totalDuration") Long totalDuration
    );
}
