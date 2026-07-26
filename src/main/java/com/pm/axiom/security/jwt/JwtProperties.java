package com.pm.axiom.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "axiom.jwt")
public record JwtProperties(String secret, long expirationMs) {
}
