package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GameRound;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, String> {

    @Query(
            "select gr from GameRound gr " +
                    "where (:createdBy is null or gr.createdBy = :createdBy) " +
                    "or (gr.createdBy = :createdBy and gr.partner.id = :userId) " +
                    "and (:searching is null or gr.title ilike '%' || cast(:searching as string ) || '%' " +
                    "or gr.description ilike '%' || cast(:searching as string ) || '%')"
    )
    Page<GameRound> findGameRounds(@Param("userId") String userId, @Param("searching") String searching, @Param("createdBy") CreatedBy createdBy, Pageable pageable);
}
