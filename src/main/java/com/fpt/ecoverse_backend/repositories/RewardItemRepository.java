package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.RewardItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardItemRepository extends JpaRepository<RewardItem, String> {

    @Query("SELECT r FROM RewardItem r WHERE r.partner.id = :partnerId")
    Page<RewardItem> findRewardItems(@Param("partnerId") String partnerId, Pageable pageable);
}
