package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.RedemptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedemptionRequestRepository extends JpaRepository<RedemptionRequest, String> {
}
