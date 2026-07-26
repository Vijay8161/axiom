package com.pm.axiom.config;

import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.entity.Role;
import com.pm.axiom.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${axiom.admin.email}")
    private String adminEmail;

    @Value("${axiom.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (recruiterRepository.existsByEmail(adminEmail)) {
            return;
        }

        Recruiter admin = Recruiter.builder()
                .name("Platform Admin")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        recruiterRepository.save(admin);
        log.info("Seeded platform ADMIN account: {}", adminEmail);
    }
}
