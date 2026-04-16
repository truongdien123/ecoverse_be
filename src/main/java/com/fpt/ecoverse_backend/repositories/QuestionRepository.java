package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findByQuizTemplateId(String quizTemplateId);

    @Query("SELECT q FROM Question q WHERE q.quizTemplate.partner.id = :partnerId")
    List<Question> findAllByPartnerId(@Param("partnerId") String partnerId);
}
