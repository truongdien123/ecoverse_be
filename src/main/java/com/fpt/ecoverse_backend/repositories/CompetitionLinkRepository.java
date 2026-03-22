package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.CompetitionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionLinkRepository extends JpaRepository<CompetitionLink, String> {
}
