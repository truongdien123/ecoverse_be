package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import com.fpt.ecoverse_backend.enums.PartnerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

    @Query("select p from Partner p where (:status is null or p.status = :status)")
    Page<Partner> findPartnersByStatus(@Param("status") PartnerStatus status, Pageable pageable);

    @Query("SELECT new com.fpt.ecoverse_backend.dtos.responses.dashboard.PartnerPointResponseDto(p.id, p.organizationName, COALESCE(SUM(s.points), 0L)) FROM Partner p LEFT JOIN p.students s GROUP BY p.id, p.organizationName")
    java.util.List<com.fpt.ecoverse_backend.dtos.responses.dashboard.PartnerPointResponseDto> getPointsSummary();
}
