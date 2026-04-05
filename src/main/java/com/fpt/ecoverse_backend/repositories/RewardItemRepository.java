package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.RewardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardItemRepository extends JpaRepository<RewardItem, String> {
}
