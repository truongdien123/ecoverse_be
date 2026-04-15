package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.base.ApiResponse;
import com.fpt.ecoverse_backend.dtos.requests.ChangePasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.ForgotPasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.LoginRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.ResetPasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentLoginRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.LoginResponseDto;
import com.fpt.ecoverse_backend.filter.CustomUserDetails;
import com.fpt.ecoverse_backend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login cho ADMIN, PARENT, PARTNERSHIP (dùng email + password + user_type)
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto<?>>> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        LoginResponseDto<?> response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>("Login successful", response));
    }

    /**
     * Login cho STUDENT (dùng student_code, không cần password)
     * POST /api/auth/student/login
     */
    @PostMapping("/student/login")
    public ResponseEntity<ApiResponse<LoginResponseDto<?>>> studentLogin(
            @Valid @RequestBody StudentLoginRequestDto request
    ) {
        LoginResponseDto<?> response = authService.studentLogin(request);
        return ResponseEntity.ok(new ApiResponse<>("Login successful", response));
    }

    /**
     * Refresh access token
     * POST /api/auth/refresh
     * Header: Authorization: Bearer <refresh_token>
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDto<?>>> refreshToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Missing or invalid Authorization header"));
        }
        String refreshToken = authHeader.substring(7);
        LoginResponseDto<?> response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>("Token refreshed successfully", response));
    }

    /**
     * Logout (stateless – client xóa token phía client là đủ)
     * POST /api/auth/logout
     * Header: Authorization: Bearer <access_token>
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            authService.logout(authHeader.substring(7));
        }
        return ResponseEntity.ok(new ApiResponse<>("Logout successful", null));
    }

    // ===================== FORGOT PASSWORD =====================

    /**
     * Bước 1 – Gửi OTP về email để reset password (KHÔNG cần đăng nhập)
     * POST /api/auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto request
    ) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(new ApiResponse<>("OTP đã được gửi đến email của bạn", null));
    }

    /**
     * Bước 2 – Verify OTP + đặt lại password mới (KHÔNG cần đăng nhập)
     * POST /api/auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDto request
    ) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponse<>("Đặt lại mật khẩu thành công", null));
    }

    // ===================== CHANGE PASSWORD =====================

    /**
     * Bước 1 – Gửi OTP về email để đổi password (CẦN đăng nhập)
     * POST /api/auth/change-password/send-otp
     * Header: Authorization: Bearer <access_token>
     */
    @PostMapping("/change-password/send-otp")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'PARENT')")
    public ResponseEntity<ApiResponse<Void>> sendChangePasswordOtp(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        authService.sendChangePasswordOtp(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>("OTP đã được gửi đến email của bạn", null));
    }

    /**
     * Bước 2 – Verify OTP + oldPassword + đổi password mới (CẦN đăng nhập)
     * POST /api/auth/change-password
     * Header: Authorization: Bearer <access_token>
     */
    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'PARENT')")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody ChangePasswordRequestDto request
    ) {
        authService.changePassword(currentUser.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>("Đổi mật khẩu thành công", null));
    }
}
