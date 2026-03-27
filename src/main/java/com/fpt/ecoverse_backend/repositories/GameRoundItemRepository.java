package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GameRoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoundItemRepository extends JpaRepository<GameRoundItem, String> {

}
