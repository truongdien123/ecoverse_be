package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select a.email from Admin a where a.email in :mails")
    Set<String> findExistingEmails(@Param("mails") Set<String> mails);
}
