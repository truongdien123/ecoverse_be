package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.WasteBin;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WasteBinRepository extends JpaRepository<WasteBin, String> {

    @Query("select wb from WasteBin wb where wb.code = :code and wb.active = true")
    Optional<WasteBin> findByCode(@Param("code") WasteBinCode code);
}
