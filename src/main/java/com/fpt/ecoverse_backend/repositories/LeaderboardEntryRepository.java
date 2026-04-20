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

    @Query(value = """
SELECT
    le.student_id as studentId,
    u.full_name as studentName,
    u.avatar_url as avatarUrl,
    s.grade as grade,
    le.points as points,
    COALESCE(ga.total_game_duration, 0) + COALESCE(qa.total_quiz_duration, 0) as minDuration

FROM leaderboard_entries le
JOIN students s ON le.student_id = s.id
JOIN users u ON s.id = u.id

LEFT JOIN (
    SELECT student_id, SUM(duration) as total_game_duration
    FROM game_attempts
    WHERE completed = true
    GROUP BY student_id
) ga ON ga.student_id = s.id

LEFT JOIN (
    SELECT student_id, SUM(duration) as total_quiz_duration
    FROM quiz_attempts
    WHERE completed = true
    GROUP BY student_id
) qa ON qa.student_id = s.id

WHERE le.partner_id = :partnerId
    AND le.scope = :scope
    AND (:grade IS NULL OR s.grade = :grade)

ORDER BY le.points DESC, minDuration ASC
""",
            countQuery = """
SELECT COUNT(*)
FROM leaderboard_entries le
JOIN students s ON le.student_id = s.id
WHERE le.partner_id = :partnerId
    AND le.scope = :scope
    AND (:grade IS NULL OR s.grade = :grade)
""",
            nativeQuery = true)
    Page<LeaderboardProjection> getLeaderboard(
            @Param("partnerId") String partnerId,
            @Param("scope") String scope,
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
