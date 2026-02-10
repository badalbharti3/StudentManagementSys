package com.example.Day3SMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
                .cors(cors -> cors.disable()) // CORS handled by @CrossOrigin annotations
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll() // Ye ni lgaya to auth p nhi ja paoge bina login kre which is senseless
                                .anyRequest().permitAll() // Allow all other requests (your JWT validation in controller will
                        // handle security) basically auth s aane vali saari  request allow krde
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions for JWT
                );

        return http.build();
    }
}