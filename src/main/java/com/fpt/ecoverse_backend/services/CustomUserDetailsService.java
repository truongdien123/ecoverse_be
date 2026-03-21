package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.enums.UserType;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByEmailAndType(String email, UserType userType);
    UserDetails loadStudentByCode(String studentCode);
    UserDetails loadAdmin(String email);
    UserDetails loadParent(String email);
    UserDetails loadPartnership(String email);
}
