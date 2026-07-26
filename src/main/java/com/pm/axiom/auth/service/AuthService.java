package com.pm.axiom.auth.service;

import com.pm.axiom.auth.dto.AuthResponse;
import com.pm.axiom.auth.dto.LoginRequest;
import com.pm.axiom.auth.dto.RegisterRequest;
import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.entity.Role;
import com.pm.axiom.exception.DuplicateResourceException;
import com.pm.axiom.repository.RecruiterRepository;
import com.pm.axiom.security.CustomUserDetails;
import com.pm.axiom.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (recruiterRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        Recruiter recruiter = Recruiter.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .companyName(request.companyName())
                .role(Role.ADMIN_RECRUITER)
                .enabled(true)
                .build();

        recruiterRepository.save(recruiter);

        String token = jwtService.generateToken(new CustomUserDetails(recruiter));
        return AuthResponse.of(token, recruiter.getId(), recruiter.getName(), recruiter.getEmail(), recruiter.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Recruiter recruiter = recruiterRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("Authenticated recruiter not found"));

        String token = jwtService.generateToken(new CustomUserDetails(recruiter));
        return AuthResponse.of(token, recruiter.getId(), recruiter.getName(), recruiter.getEmail(), recruiter.getRole());
    }
}
