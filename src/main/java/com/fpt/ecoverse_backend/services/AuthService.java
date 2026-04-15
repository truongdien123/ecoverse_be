package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.ChangePasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.ForgotPasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.LoginRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.ResetPasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentLoginRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.LoginResponseDto;

public interface AuthService {

    LoginResponseDto<?> login(LoginRequestDto request);

    LoginResponseDto<?> studentLogin(StudentLoginRequestDto request);

    LoginResponseDto<?> refreshToken(String refreshToken);

    void logout(String accessToken);

    // ===================== OTP =====================

    /**
     * Bước 1 – Forgot Password: gửi OTP đến email
     */
    void forgotPassword(ForgotPasswordRequestDto request);

    /**
     * Bước 2 – Forgot Password: verify OTP + đặt lại password mới
     */
    void resetPassword(ResetPasswordRequestDto request);

    /**
     * Bước 1 – Change Password (đã đăng nhập): gửi OTP đến email của user
     */
    void sendChangePasswordOtp(String userId);

    /**
     * Bước 2 – Change Password (đã đăng nhập): verify OTP + oldPassword + đổi password
     */
    void changePassword(String userId, ChangePasswordRequestDto request);
}
