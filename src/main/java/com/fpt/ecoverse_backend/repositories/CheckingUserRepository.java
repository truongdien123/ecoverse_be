package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CheckingUserRepository extends JpaRepository<Partner, String> {

    @Query(
            value = "select pr.email from parents pr where pr.email in (:emails) " +
                    "union " +
                    "select pn.email from partners pn where pn.email in (:emails) " +
                    "union " +
                    "select ad.email from admins ad where ad.email in (:emails)",
            nativeQuery = true
    )
    Set<String> findExistingEmail(@Param("emails") Set<String> emails);
}
