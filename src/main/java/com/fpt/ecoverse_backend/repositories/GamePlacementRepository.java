package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GamePlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamePlacementRepository extends JpaRepository<GamePlacement, String> {

    @Query("SELECT gp FROM GamePlacement gp WHERE gp.gameAttempt.id = :gameAttemptId")
    List<GamePlacement> findByGameAttemptId(@Param("gameAttemptId") String gameAttemptId);
}
