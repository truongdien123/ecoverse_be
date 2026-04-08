package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GameRoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRoundItemRepository extends JpaRepository<GameRoundItem, String> {

    @Query("SELECT gri FROM GameRoundItem gri WHERE gri.gameRound.id = :gameRoundId AND gri.wasteItem.id = :wasteItemId")
    Optional<GameRoundItem> findByGameRoundIdAndWasteItemId(@Param("gameRoundId") String gameRoundId, @Param("wasteItemId") String wasteItemId);


}
