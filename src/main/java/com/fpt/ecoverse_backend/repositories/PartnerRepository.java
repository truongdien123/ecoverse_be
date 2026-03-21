package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Partner> findByEmail(String email);

    @Query("select p.email from Partner p where p.email in :emails")
    Set<String> findExistingEmails(@Param("emails") Set<String> emails);
}
