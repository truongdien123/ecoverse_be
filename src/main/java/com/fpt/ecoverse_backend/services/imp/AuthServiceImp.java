package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.LoginRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentLoginRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.*;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.filter.CustomUserDetails;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.services.AuthService;
import com.fpt.ecoverse_backend.services.CustomUserDetailsService;
import com.fpt.ecoverse_backend.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImp(JwtUtils jwtUtils,
                          CustomUserDetailsService customUserDetailsService,
                          UserRepository userRepository,
                          StudentRepository studentRepository,
                          PartnerRepository partnerRepository,
                          PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.partnerRepository = partnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDto<?> login(LoginRequestDto request) {
        // STUDENT không dùng endpoint này
        if (request.getUserType() == UserType.STUDENT) {
            throw new BadCredentialsException("Students must use student login endpoint");
        }

        // Load user details theo email + role
        UserDetails userDetails = customUserDetailsService.loadUserByEmailAndType(
                request.getEmail(), request.getUserType()
        );

        CustomUserDetails customUser = (CustomUserDetails) userDetails;

        // Kiểm tra account active
        if (!customUser.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }

        // Kiểm tra password
        if (!passwordEncoder.matches(request.getPassword(), customUser.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // Generate tokens
        String accessToken = jwtUtils.generateAccessToken(customUser.getId(), customUser.getUsername(), customUser.getUserType());
        String refreshToken = jwtUtils.generateRefreshToken(customUser.getId(), customUser.getUserType());

        // Build user info response theo role
        Object userInfo = buildUserInfo(customUser);

        return new LoginResponseDto<>(
                accessToken,
                refreshToken,
                jwtUtils.getExpirationInSeconds(),
                customUser.getUserType(),
                userInfo
        );
    }

    @Override
    public LoginResponseDto<?> studentLogin(StudentLoginRequestDto request) {
        // Load student by student_code (không cần password)
        UserDetails userDetails = customUserDetailsService.loadStudentByCode(request.getStudentCode());
        CustomUserDetails customUser = (CustomUserDetails) userDetails;

        if (!customUser.isEnabled()) {
            throw new DisabledException("Student account is disabled");
        }

        Student student = studentRepository.findByStudentCode(request.getStudentCode())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        User user = userRepository.findById(student.getId())
                .orElseThrow(() -> new NotFoundException("User not found for student"));

        String accessToken = jwtUtils.generateAccessToken(customUser.getId(), customUser.getUsername() != null ? customUser.getUsername() : "", UserType.STUDENT);
        String refreshToken = jwtUtils.generateRefreshToken(customUser.getId(), UserType.STUDENT);

        StudentResponseDto studentInfo = buildStudentInfo(user, student);

        return new LoginResponseDto<>(
                accessToken,
                refreshToken,
                jwtUtils.getExpirationInSeconds(),
                UserType.STUDENT,
                studentInfo
        );
    }

    @Override
    public LoginResponseDto<?> refreshToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }

        // Kiểm tra đúng là REFRESH token
        String tokenType = jwtUtils.extractClaim(refreshToken, claims -> claims.get("tokenType", String.class));
        if (!"REFRESH".equals(tokenType)) {
            throw new BadCredentialsException("Token is not a refresh token");
        }

        String userId = jwtUtils.extractUserId(refreshToken);
        UserType userType = jwtUtils.extractUserType(refreshToken);

        // Load user để lấy email
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getActive() == null || !user.getActive()) {
            throw new DisabledException("Account is disabled");
        }

        String newAccessToken = jwtUtils.generateAccessToken(userId, user.getEmail() != null ? user.getEmail() : "", userType);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId, userType);

        return new LoginResponseDto<>(
                newAccessToken,
                newRefreshToken,
                jwtUtils.getExpirationInSeconds(),
                userType,
                null
        );
    }

    @Override
    public void logout(String accessToken) {
        // Stateless JWT: không cần invalidate token phía server
        // Nếu sau này có blacklist thì implement ở đây
    }

    // ======================== PRIVATE HELPERS ========================

    private Object buildUserInfo(CustomUserDetails customUser) {
        return switch (customUser.getUserType()) {
            case ADMIN -> {
                User user = userRepository.findById(customUser.getId())
                        .orElseThrow(() -> new NotFoundException("Admin not found"));
                yield new AdminResponseDto(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getPhoneNumber(),
                        user.getAvatarUrl()
                );
            }
            case PARENT -> {
                User user = userRepository.findById(customUser.getId())
                        .orElseThrow(() -> new NotFoundException("Parent not found"));
                yield new ParentResponseDto(
                        user.getId(),
                        user.getFullName(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        user.getEmail(),
                        user.getAvatarUrl(),
                        user.getActive(),
                        null
                );
            }
            case PARTNERSHIP -> {
                User user = userRepository.findById(customUser.getId())
                        .orElseThrow(() -> new NotFoundException("Partner not found"));
                Partner partner = partnerRepository.findById(customUser.getId())
                        .orElseThrow(() -> new NotFoundException("Partner profile not found"));
                yield new PartnerResponseDto(
                        partner.getId(),
                        partner.getOrganizationName(),
                        user.getEmail(),
                        partner.getContactPerson(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        partner.getStatus(),
                        user.getAvatarUrl(),
                        user.getCreatedAt(),
                        user.getUpdatedAt(),
                        null
                );
            }
            default -> throw new BadCredentialsException("Unsupported user type");
        };
    }

    private StudentResponseDto buildStudentInfo(User user, Student student) {
        return new StudentResponseDto(
                student.getId(),
                user.getFullName(),
                student.getStudentCode(),
                student.getGrade(),
                student.getPoints() != null ? student.getPoints() : 0,
                user.getAvatarUrl(),
                null,
                null,
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getActive() != null && user.getActive()
        );
    }
}
