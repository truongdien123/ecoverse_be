package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.QuizTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizTemplateRepository extends JpaRepository<QuizTemplate, String> {

    @Query("SELECT qt FROM QuizTemplate qt WHERE qt.partner.id = :partnerId AND qt.active = true " +
           "AND (:title IS NULL OR lower(qt.title) LIKE lower(concat('%', :title, '%')))")
    Page<QuizTemplate> findByPartnerIdAndActiveTrue(
            @Param("partnerId") String partnerId,
            @Param("title") String title,
            Pageable pageable);

    @Query("SELECT qt FROM QuizTemplate qt WHERE qt.id = :id AND qt.partner.id = :partnerId")
    Optional<QuizTemplate> findByIdAndPartnerId(@Param("id") String id, @Param("partnerId") String partnerId);

    @Query("SELECT qt FROM QuizTemplate qt WHERE qt.active = true " +
           "AND (:title IS NULL OR lower(qt.title) LIKE lower(concat('%', :title, '%')))")
    Page<QuizTemplate> findAllActive(@Param("title") String title, Pageable pageable);
}
