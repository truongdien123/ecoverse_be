package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.CompetitionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, String> {

    @Query("select cp from CompetitionParticipant cp where cp.competition.id = :competitionId")
    List<CompetitionParticipant> findByCompetitionId(@Param("competitionId") String competitionId);
}
