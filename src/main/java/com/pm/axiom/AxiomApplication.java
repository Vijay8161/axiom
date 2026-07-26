package com.pm.axiom;

import com.pm.axiom.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class AxiomApplication {
    public static void main(String[] args) {
        SpringApplication.run(AxiomApplication.class, args);
    }
}
