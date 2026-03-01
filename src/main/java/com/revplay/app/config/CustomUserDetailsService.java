package com.revplay.app.config;

import com.revplay.app.entity.User;
import com.revplay.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrId) throws UsernameNotFoundException {
        User user = null;

        // JWT Filter passes the Subject (which is the User ID). Try ID first.
        try {
            Long userId = Long.valueOf(usernameOrEmailOrId);
            user = userRepository.findById(userId).orElse(null);
        } catch (NumberFormatException e) {
            // Not an ID, proceed to username/email
        }

        if (user == null && usernameOrEmailOrId.contains("@")) {
            user = userRepository.findByEmail(usernameOrEmailOrId).orElse(null);
        }

        if (user == null) {
            user = userRepository.findByUsername(usernameOrEmailOrId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmailOrId));
        }

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()), // Storing User ID as username for JWT reference
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())));
    }
}
