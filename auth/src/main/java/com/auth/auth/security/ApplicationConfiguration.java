package com.auth.auth.security;

import com.auth.auth.entity.Users;
import com.auth.auth.repository.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class ApplicationConfiguration {

    private final UsersRepository usersRepository;

    public ApplicationConfiguration(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // Takes: username/email
    // Returns: UserDetails
    // Called by: DaoAuthenticationProvider during authentication
    // Purpose: Load user from database by email or username/mobileNo
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Users user = usersRepository.findByEmailId(username);
            if (user == null) {
                user = usersRepository.findByUserNameOrMobileNo(username, username);
            }
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getEmailId(),
                    user.getPassword(),
                    new ArrayList<>() // roles/authorities (empty for now)
            );
        };
    }

    // Takes: none
    // Returns: PasswordEncoder
    // Called by: DaoAuthenticationProvider
    // Purpose: Encode passwords (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Takes: none
    // Returns: AuthenticationProvider
    // Called by: SecurityConfig
    // Purpose: Authenticate users using UserDetailsService + PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Takes: AuthenticationConfiguration
    // Returns: AuthenticationManager
    // Called by: Spring Security automatically
    // Purpose: Expose AuthenticationManager for authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
