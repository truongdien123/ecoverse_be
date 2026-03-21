package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.WasteItem;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteItemRepository extends JpaRepository<WasteItem, String> {

    @Query("select w from WasteItem w where w.createdBy = :role or w.partner.id = :userId")
    List<WasteItem> findWasteItems(CreatedBy role, String userId);
}
