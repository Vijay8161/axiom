package com.pm.axiom.security;

import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RecruiterRepository recruiterRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Recruiter recruiter = recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No account found for email: " + email));
        return new CustomUserDetails(recruiter);
    }
}