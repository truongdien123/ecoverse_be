package com.fpt.ecoverse_backend.components;

import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedAdmin();
    }

    private void seedAdmin() {
        String adminEmail = "admin@ecoverse.com";

        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.info("Admin already exists, skipping seed.");
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setFullName("Super Admin");
        admin.setPhoneNumber("0900000000");
        admin.setRole(UserType.ADMIN);
        admin.setActive(true);

        userRepository.save(admin);
        log.info("✅ Admin seeded: email={}, password=Admin@123", adminEmail);
    }
}
