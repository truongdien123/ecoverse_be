package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.OtpToken;
import com.fpt.ecoverse_backend.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, String> {

    /**
     * Lấy OTP mới nhất còn hiệu lực của email + purpose
     */
    Optional<OtpToken> findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(
            String email, OtpPurpose purpose);

    /**
     * Vô hiệu hóa toàn bộ OTP cũ của email + purpose (trước khi tạo OTP mới)
     */
    @Modifying
    @Transactional
    @Query("UPDATE OtpToken o SET o.used = true WHERE o.email = :email AND o.purpose = :purpose AND o.used = false")
    void invalidateAllByEmailAndPurpose(@Param("email") String email, @Param("purpose") OtpPurpose purpose);
}
