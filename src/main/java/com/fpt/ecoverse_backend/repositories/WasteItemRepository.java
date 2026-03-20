package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.WasteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteItemRepository extends JpaRepository<WasteItem, String> {
}
