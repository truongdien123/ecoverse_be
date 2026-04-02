package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.LoginRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentLoginRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.LoginResponseDto;

public interface AuthService {

    LoginResponseDto<?> login(LoginRequestDto request);

    LoginResponseDto<?> studentLogin(StudentLoginRequestDto request);

    LoginResponseDto<?> refreshToken(String refreshToken);

    void logout(String accessToken);
}
