package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.filter.CustomUserDetails;
import com.fpt.ecoverse_backend.repositories.ParentRepository;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.services.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImp implements CustomUserDetailsService, UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PartnerRepository partnerRepository;

    public CustomUserDetailsServiceImp(UserRepository userRepository, StudentRepository studentRepository, ParentRepository parentRepository, PartnerRepository partnerRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.partnerRepository = partnerRepository;
    }


    @Override
    public UserDetails loadUserByEmailAndType(String emailOrId, UserType userType) {
        return switch (userType) {
            case ADMIN -> loadAdmin(emailOrId);
            case PARENT -> loadParent(emailOrId);
            case PARTNERSHIP -> loadPartnership(emailOrId);
            case STUDENT -> loadStudentById(emailOrId);
        };
    }

    @Override
    public UserDetails loadStudent(String studentCode) {
        var student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        Optional<User> userOpt = userRepository.findById(student.getId());
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        } else if (userOpt.get().getRole() != UserType.STUDENT) {
            throw new BadRequestException("User is not a student");
        }
        return CustomUserDetails.builder()
                .id(student.getId())
                .email(null)
                .password(null)
                .fullName(userOpt.get().getFullName())
                .avatarUrl(userOpt.get().getAvatarUrl())
                .userType(UserType.STUDENT)
                .active(userOpt.get().getActive() != null ? userOpt.get().getActive() : true)
                .build();
    }

    @Override
    public UserDetails loadStudentByCode(String studentCode) {
        var student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        Optional<User> userOpt = userRepository.findById(student.getId());
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return CustomUserDetails.builder()
                .id(student.getId())
                .email(null)
                .password(null)
                .fullName(userOpt.get().getFullName())
                .avatarUrl(userOpt.get().getAvatarUrl())
                .userType(UserType.STUDENT)
                .active(userOpt.get().getActive() != null ? userOpt.get().getActive() : true)
                .build();
    }


    @Override
    public UserDetails loadAdmin(String email) {
        var admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Admin not found with email: " + email));
                
        if (admin.getRole() != UserType.ADMIN) {
            throw new org.springframework.security.authentication.BadCredentialsException("Mismatch user type. This account is not an Admin");
        }

        return CustomUserDetails.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .password(admin.getPassword())
                .fullName(admin.getFullName())
                .avatarUrl(admin.getAvatarUrl())
                .userType(UserType.ADMIN)
                .active(true)
                .build();
    }

    @Override
    public UserDetails loadParent(String email) {
        var parent = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Parent not found with email: " + email));
                
        if (parent.getRole() != UserType.PARENT) {
            throw new org.springframework.security.authentication.BadCredentialsException("Mismatch user type. This account is not a Parent");
        }
                
        return CustomUserDetails.builder()
                .id(parent.getId())
                .email(parent.getEmail())
                .password(parent.getPassword())
                .fullName(parent.getFullName())
                .avatarUrl(parent.getAvatarUrl())
                .userType(UserType.PARENT)
                .active(parent.getActive() != null ? parent.getActive() : true)
                .build();
    }

    @Override
    public UserDetails loadPartnership(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
                
        if (user.getRole() != UserType.PARTNERSHIP) {
            throw new org.springframework.security.authentication.BadCredentialsException("Mismatch user type. This account is not a Partner");
        }
                
        Optional<Partner> partner = partnerRepository.findById(user.getId());
        if (partner.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .fullName(partner.get().getOrganizationName())
                .avatarUrl(user.getAvatarUrl())
                .userType(UserType.PARTNERSHIP)
                .active(true)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Use loadUserByEmailAndType instead");
    }

    private UserDetails loadStudentById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Student user not found with id: " + userId));
        return CustomUserDetails.builder()
                .id(user.getId())
                .email(null)
                .password(null)
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .userType(UserType.STUDENT)
                .active(user.getActive() != null ? user.getActive() : true)
                .build();
    }
}
