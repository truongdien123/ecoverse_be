package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

}
