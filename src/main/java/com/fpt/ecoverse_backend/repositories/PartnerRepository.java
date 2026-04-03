package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

    @Query("select p from Partner p where (:status is null or p.status = :status)")
    Page<Partner> findParntersByStatus(@Param("status") String status, Pageable pageable);
}
