package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = :email OR u.phoneNumber = :phoneNumber")
    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.email IN :emails")
    Set<String> findExistingEmail(@Param("emails") Set<String> emails);
}
