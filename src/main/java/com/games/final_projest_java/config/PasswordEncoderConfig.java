package com.games.final_projest_java.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
 
/**
 * Separate config class for PasswordEncoder.
 * This breaks the circular dependency:
 *   SecurityConfig -> UserService -> PasswordEncoder -> SecurityConfig (CYCLE!)
 * By moving PasswordEncoder here, UserService can get it without touching SecurityConfig.
 */
@Configuration
public class PasswordEncoderConfig {
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
