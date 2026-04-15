package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findByQuizTemplateId(String quizTemplateId);
}
