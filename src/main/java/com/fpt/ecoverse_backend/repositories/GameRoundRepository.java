package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.GameRound;
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
                    "left join gr.partner p " +
                    "where (:createdBy is null or gr.createdBy = :createdBy) " +
                    "and (gr.partner.id = :userId) or (gr.createdBy = 'PARTNERSHIP' and p.id = :userId) " +
                    "and (:searching is null or lower(gr.title) like lower(concat('%', :searching, '%')) " +
                    "or lower(gr.description) like lower(concat('%', :searching, '%')))"
    )
    Page<GameRound> findGameRounds(@Param("userId") String userId, @Param("searching") String searching, @Param("createdBy") String createdBy, Pageable pageable);
}
