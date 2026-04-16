package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.ParentCredentialMail;

import java.util.List;

public interface MailService {
    void sendParentWelcomeBatch(List<ParentCredentialMail> mails);

    /**
     * Gửi mail OTP cho forgot-password hoặc change-password
     * @param email         địa chỉ email nhận
     * @param fullName      tên hiển thị trong mail
     * @param otpCode       mã OTP 4-6 chữ số
     * @param purposeMessage mô tả mục đích (hiện trong body mail)
     */
    void sendOtpMail(String email, String fullName, String otpCode, String purposeMessage);

    void sendRewardRequestEmail(
            String toEmail,
            String studentName,
            String rewardName,
            int points,
            String requestTime
    );
}
