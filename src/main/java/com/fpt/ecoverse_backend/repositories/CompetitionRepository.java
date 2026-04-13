package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {

    @Query("select c from Competition c where c.partner.id = :partnerId")
    List<Competition> findByPartnerId(@Param("partnerId") String partnerId);
}
