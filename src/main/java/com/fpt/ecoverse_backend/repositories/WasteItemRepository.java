package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.WasteItem;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import com.fpt.ecoverse_backend.projections.WasteItemWithOrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WasteItemRepository extends JpaRepository<WasteItem, String> {

    @Query("select w from WasteItem w where w.createdBy = :role or w.partner.id = :userId")
    List<WasteItem> findWasteItems(CreatedBy role, String userId);

    @Query("select w from WasteItem w where w.partner.id = :partnerId")
    Optional<WasteItem> findWasteItemByPartnerId(String partnerId);

    @Query("select w from WasteItem w where w.id in :ids")
    List<WasteItem> findWasteItemByIds(List<String> ids);

    @Query("select w as wasteItem, gri.orderIndex as orderIndex " +
            "from GameRoundItem gri join gri.wasteItem w " +
            "where gri.gameRound.id = :gameRoundId and gri.gameRound.partner.id = :userId")
    List<WasteItemWithOrderProjection> findByUserIdAndGameRoundId(@Param("userId") String userId, @Param("gameRoundId") String gameRoundId);


}
