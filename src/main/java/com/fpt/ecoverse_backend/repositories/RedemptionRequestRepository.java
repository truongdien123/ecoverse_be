package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.RedemptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedemptionRequestRepository extends JpaRepository<RedemptionRequest, String> {

    @Query("SELECT r FROM RedemptionRequest r WHERE r.student.id = :studentId")
    List<RedemptionRequest> findByStudentId(@Param("studentId") String studentId);

    @Query("SELECT r FROM RedemptionRequest r WHERE r.parent.id = :parentId")
    List<RedemptionRequest> findByParentId(@Param("parentId") String parentId);

    @Query("SELECT r FROM RedemptionRequest r WHERE r.partner.id = :partnerId")
    List<RedemptionRequest> findByPartnerId(@Param("partnerId") String partnerId);

    @Query("select r from RedemptionRequest r where r.partner.id = :partnerId")
    long countByPartnerId(@Param("partnerId") String partnerId);
}
