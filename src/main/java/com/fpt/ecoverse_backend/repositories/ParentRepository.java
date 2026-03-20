package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ParentRepository extends JpaRepository<Parent, String> {

    @Query("select count(p.id) from Parent p where p.partner.id = :partnerId")
    long countParentsByPartnerId(@Param("partnerId") String partnerId);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select p.email from Parent p where p.email in :emails")
    Set<String> findExistingEmails(@Param("emails") Set<String> mails);

    @Query(
            "select p from Parent p " +
                    "where p.partner.id = :partnerId " +
                    "and (:searching is null or lower(p.fullName) like lower(concat('%',:searching,'%'))) " +
                    "and (:hasChildren = false or (:hasChildren = true " +
                    "and exists (select s from Student s where s.parent.id = p.id)))"
    )
    Page<Parent> searchParents(@Param("partnerId") String partnerId, @Param("searching") String searching, @Param("hasChildren") boolean hasChildren, Pageable pageable);
}
