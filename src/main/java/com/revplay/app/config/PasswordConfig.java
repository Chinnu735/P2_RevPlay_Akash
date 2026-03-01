package com.revplay.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Warning: Using NoOpPasswordEncoder strictly because the legacy DB records
        // have plaintext passwords.
        // In a real production system, you must migrate to BCryptPasswordEncoder.
        return NoOpPasswordEncoder.getInstance();
    }
}
