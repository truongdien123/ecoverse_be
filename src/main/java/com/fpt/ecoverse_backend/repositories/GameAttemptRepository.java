package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GameAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameAttemptRepository extends JpaRepository<GameAttempt, String> {

    @Query("select ga from GameAttempt ga where ga.gameRound.id = :gameRoundId and ga.student.id = :studentId")
    List<GameAttempt> findByGameGroundAndStudent(@Param("gameRoundId") String gameRoundId, @Param("studentId") String studentId);

    @Query("select ga from GameAttempt ga where ga.student.id = :studentId")
    Page<GameAttempt> findGameAttempts(@Param("studentId") String studentId, Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(ga.duration), 0)
    FROM GameAttempt ga
    WHERE ga.student.id = :studentId
        AND ga.completed = true
""")
    Long getTotalDurationByStudent(@Param("studentId") String studentId);

    @Query("""
    SELECT ga
    FROM GameAttempt ga
    WHERE ga.id IN (
        SELECT MIN(sub.id)
        FROM GameAttempt sub
        WHERE sub.student.id = :studentId
        GROUP BY sub.gameRound.id
    )
""")
    List<GameAttempt> findDistinctByGameRound(@Param("studentId") String studentId);

    @Query("""
    SELECT COALESCE(SUM(ga.pointsEarned), 0)
    FROM GameAttempt ga
    WHERE ga.student.partner.id = :partnerId and ga.completed = true
""")
    Long sumGamePoints(@Param("partnerId") String partnerId);
}
