package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.QuizPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizPlacementRepository extends JpaRepository<QuizPlacement, String> {
}
