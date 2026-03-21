package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.filter.CustomUserDetails;
import com.fpt.ecoverse_backend.repositories.AdminRepository;
import com.fpt.ecoverse_backend.repositories.ParentRepository;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.services.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImp implements CustomUserDetailsService, UserDetailsService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PartnerRepository partnerRepository;

    public CustomUserDetailsServiceImp(AdminRepository adminRepository, StudentRepository studentRepository, ParentRepository parentRepository, PartnerRepository partnerRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.partnerRepository = partnerRepository;
    }


    @Override
    public UserDetails loadUserByEmailAndType(String email, UserType userType) {
        return switch (userType) {
            case ADMIN -> loadAdmin(email);
            case PARENT -> loadParent(email);
            case PARTNERSHIP -> loadPartnership(email);
            case STUDENT -> throw new NotFoundException("Students should use student code to login");
        };
    }

    @Override
    public UserDetails loadStudentByCode(String studentCode) {
        var student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new NotFoundException("Student not found with code: " + studentCode));

        return CustomUserDetails.builder()
                .id(student.getId())
                .email(null)
                .password(null)
                .fullName(student.getFullName())
                .avatarUrl(student.getAvatarUrl())
                .userType(UserType.STUDENT)
                .active(student.getActive() != null ? student.getActive() : true)
                .build();
    }


    @Override
    public UserDetails loadAdmin(String email) {
        var admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Admin not found with email: " + email));

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
        var parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Parent not found with email: " + email));

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
        var partnership = partnerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Partnership not found with email: " + email));

        return CustomUserDetails.builder()
                .id(partnership.getId())
                .email(partnership.getEmail())
                .password(partnership.getPassword())
                .fullName(partnership.getOrganizationName())
                .avatarUrl(partnership.getAvatarUrl())
                .userType(UserType.PARTNERSHIP)
                .active(true)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Use loadUserByEmailAndType instead");
    }
}
