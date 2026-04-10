package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GameAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameAttemptRepository extends JpaRepository<GameAttempt, String> {

    @Query("select ga from GameAttempt ga where ga.gameRound.id = :gameRoundId and ga.student.id = :studentId")
    Optional<GameAttempt> findByGameGroundAndStudent(@Param("gameRoundId") String gameRoundId, @Param("studentId") String studentId);

    @Query("select ga from GameAttempt ga where ga.student.id = :studentId")
    Page<GameAttempt> findGameAttempts(@Param("studentId") String studentId, Pageable pageable);
}
