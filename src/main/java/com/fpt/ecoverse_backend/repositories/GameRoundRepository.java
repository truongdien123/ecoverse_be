package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, String> {
}
