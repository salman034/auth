package com.auth.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter,
                          RequestLoggingFilter requestLoggingFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Takes: HttpSecurity object (Spring Security)
    // Returns: SecurityFilterChain
    // Called by: Spring Boot automatically during application startup
    // Purpose: Configures Spring Security, endpoints, filters, authentication provider
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Disable CSRF for stateless REST APIs
        http.csrf(AbstractHttpConfigurer::disable);

        // Set session to stateless (no HTTP session)
        http.sessionManagement(sm -> sm
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Set endpoint access rules
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/auth/**").permitAll() // public endpoints
                .anyRequest().authenticated() // all other endpoints require auth
        );

        // Allow frames for H2-console or similar
        http.headers(h -> h.frameOptions(f -> f.sameOrigin()));

        // Set custom authentication provider
        http.authenticationProvider(authenticationProvider);

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
