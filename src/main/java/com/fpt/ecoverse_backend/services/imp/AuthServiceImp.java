package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.ChangePasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.ForgotPasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.LoginRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.ResetPasswordRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentLoginRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.*;
import com.fpt.ecoverse_backend.entities.OtpToken;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.enums.OtpPurpose;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.filter.CustomUserDetails;
import com.fpt.ecoverse_backend.repositories.OtpTokenRepository;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.services.AuthService;
import com.fpt.ecoverse_backend.services.CustomUserDetailsService;
import com.fpt.ecoverse_backend.services.MailService;
import com.fpt.ecoverse_backend.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthServiceImp implements AuthService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpTokenRepository otpTokenRepository;
    private final MailService mailService;

    public AuthServiceImp(JwtUtils jwtUtils,
                          CustomUserDetailsService customUserDetailsService,
                          UserRepository userRepository,
                          StudentRepository studentRepository,
                          PartnerRepository partnerRepository,
                          PasswordEncoder passwordEncoder,
                          OtpTokenRepository otpTokenRepository,
                          MailService mailService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.partnerRepository = partnerRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpTokenRepository = otpTokenRepository;
        this.mailService = mailService;
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
        UserDetails userDetails = customUserDetailsService.loadStudent(request.getStudentCode());
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

    // ======================== FORGOT PASSWORD ========================

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequestDto request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email không tồn tại trong hệ thống"));

        // Student không có password → không hỗ trợ
        if (user.getRole() == UserType.STUDENT) {
            throw new BadRequestException("Tài khoản Student không hỗ trợ tính năng này");
        }

        // Vô hiệu hóa OTP cũ trước khi tạo mới
        otpTokenRepository.invalidateAllByEmailAndPurpose(email, OtpPurpose.FORGOT_PASSWORD);

        // Tạo và lưu OTP mới
        String otpCode = generateOtp();
        OtpToken otpToken = new OtpToken();
        otpToken.setEmail(email);
        otpToken.setOtpCode(otpCode);
        otpToken.setPurpose(OtpPurpose.FORGOT_PASSWORD);
        otpToken.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        otpToken.setUsed(false);
        otpTokenRepository.save(otpToken);

        // Gửi mail bất đồng bộ
        mailService.sendOtpMail(
                email,
                user.getFullName(),
                otpCode,
                "Bạn vừa yêu cầu đặt lại mật khẩu tài khoản EcoVerse."
        );
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDto request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email không tồn tại trong hệ thống"));

        // Validate OTP
        OtpToken otpToken = otpTokenRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email, OtpPurpose.FORGOT_PASSWORD)
                .orElseThrow(() -> new BadRequestException("Mã OTP không hợp lệ"));

        if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã OTP đã hết hạn");
        }
        if (!otpToken.getOtpCode().equals(request.getOtp())) {
            throw new BadRequestException("Mã OTP không chính xác");
        }

        // Đặt lại mật khẩu
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Đánh dấu OTP đã dùng
        otpToken.setUsed(true);
        otpTokenRepository.save(otpToken);
    }

    // ======================== CHANGE PASSWORD ========================

    @Override
    @Transactional
    public void sendChangePasswordOtp(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() == UserType.STUDENT) {
            throw new BadRequestException("Tài khoản Student không hỗ trợ tính năng này");
        }

        String email = user.getEmail();

        // Vô hiệu hóa OTP cũ
        otpTokenRepository.invalidateAllByEmailAndPurpose(email, OtpPurpose.CHANGE_PASSWORD);

        // Tạo OTP mới
        String otpCode = generateOtp();
        OtpToken otpToken = new OtpToken();
        otpToken.setEmail(email);
        otpToken.setOtpCode(otpCode);
        otpToken.setPurpose(OtpPurpose.CHANGE_PASSWORD);
        otpToken.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        otpToken.setUsed(false);
        otpTokenRepository.save(otpToken);

        // Gửi mail bất đồng bộ
        mailService.sendOtpMail(
                email,
                user.getFullName(),
                otpCode,
                "Bạn vừa yêu cầu đổi mật khẩu tài khoản EcoVerse."
        );
    }

    @Override
    @Transactional
    public void changePassword(String userId, ChangePasswordRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String email = user.getEmail();

        // Validate OTP
        OtpToken otpToken = otpTokenRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email, OtpPurpose.CHANGE_PASSWORD)
                .orElseThrow(() -> new BadRequestException("Mã OTP không hợp lệ"));

        if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã OTP đã hết hạn");
        }
        if (!otpToken.getOtpCode().equals(request.getOtp())) {
            throw new BadRequestException("Mã OTP không chính xác");
        }

        // Validate mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu hiện tại không chính xác");
        }

        // Không cho phép đặt lại mật khẩu giống cũ
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu mới không được trùng với mật khẩu hiện tại");
        }

        // Cập nhật mật khẩu
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Đánh dấu OTP đã dùng
        otpToken.setUsed(true);
        otpTokenRepository.save(otpToken);
    }

    // ======================== PRIVATE HELPERS ========================

    private String generateOtp() {
        // Sinh số ngẫu nhiên 6 chữ số: 100000 → 999999
        int otp = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }
}
