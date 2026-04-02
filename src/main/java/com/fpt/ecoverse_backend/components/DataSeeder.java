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
import org.springframework.beans.factory.annotation.Value;

@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmailStr;

    @Value("${app.admin.password}")
    private String adminPasswordStr;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedAdmin();
    }

    private void seedAdmin() {
        if (userRepository.findByEmail(adminEmailStr).isPresent()) {
            log.info("Admin already exists, skipping seed.");
            return;
        }
        User admin = new User();
        admin.setEmail(adminEmailStr);
        admin.setPassword(passwordEncoder.encode(adminPasswordStr));
        admin.setFullName("Super Admin");
        admin.setPhoneNumber("0900000000");
        admin.setRole(UserType.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);
        log.info("✅ Admin seeded: email={}, password=***", adminEmailStr);
    }
}
