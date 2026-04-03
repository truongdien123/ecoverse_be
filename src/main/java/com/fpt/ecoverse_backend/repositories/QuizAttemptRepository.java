package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.QuizAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, String> {

    @Query("SELECT COUNT(qa) FROM QuizAttempt qa WHERE qa.quizTemplate.id = :templateId AND qa.student.id = :studentId")
    int countByTemplateAndStudent(@Param("templateId") String templateId, @Param("studentId") String studentId);

    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.student.id = :studentId")
    Page<QuizAttempt> findByStudentId(@Param("studentId") String studentId, Pageable pageable);

    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.quizTemplate.id = :templateId ORDER BY qa.createdAt DESC")
    List<QuizAttempt> findByTemplateId(@Param("templateId") String templateId);
}
