package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.base.ApiResponse;
import com.fpt.ecoverse_backend.dtos.requests.LoginRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentLoginRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.LoginResponseDto;
import com.fpt.ecoverse_backend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
}
