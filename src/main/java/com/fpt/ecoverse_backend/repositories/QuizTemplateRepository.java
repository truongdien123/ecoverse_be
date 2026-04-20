package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.QuizTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizTemplateRepository extends JpaRepository<QuizTemplate, String> {

    @Query("SELECT qt FROM QuizTemplate qt WHERE qt.partner.id = :partnerId AND qt.active = true and qt.isCompetition = false " +
           "AND (:title IS NULL OR qt.title ILIKE '%' || cast(:title as string ) || '%')")
    Page<QuizTemplate> findByPartnerIdAndActiveTrue(
            @Param("partnerId") String partnerId,
            @Param("title") String title,
            Pageable pageable);

    @Query("SELECT qt FROM QuizTemplate qt WHERE qt.id = :id AND qt.partner.id = :partnerId and qt.active = true")
    Optional<QuizTemplate> findByIdAndPartnerId(@Param("id") String id, @Param("partnerId") String partnerId);

    @Query("SELECT qt FROM QuizTemplate qt WHERE qt.active = true " +
           "AND (:title IS NULL OR qt.title ILIKE '%' || cast(:title as string ) || '%')")
    Page<QuizTemplate> findAllActive(@Param("title") String title, Pageable pageable);

    @Query("SELECT qt FROM QuizTemplate qt WHERE qt.id = :quizTemplateId and qt.isCompetition = true and qt.active = true")
    Optional<QuizTemplate> findByIdForCompetition(@Param("quizTemplateId") String quizTemplateId);

    @Query("select count(q.id) from QuizTemplate q where q.partner.id = :partnerId and q.active = true")
    long countByPartnerId(@Param("partnerId") String partnerId);

    @Query("select qt from QuizTemplate qt where qt.partner.id = :partnerId and qt.isCompetition = true and qt.active = true")
    List<QuizTemplate> findQuizTemplateCompetition(@Param("partnerId") String partnerId);
}
