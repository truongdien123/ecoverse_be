package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.CompetitionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompetitionLinkRepository extends JpaRepository<CompetitionLink, String> {

    @Query("select cl from CompetitionLink cl where cl.competition.id = :competitionId")
    Optional<CompetitionLink> findByCompetitionId(@Param("competitionId") String competitionId);
}
