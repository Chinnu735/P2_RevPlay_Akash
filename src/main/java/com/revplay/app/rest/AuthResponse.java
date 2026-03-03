package com.revplay.app.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private Long userId;
    private String email;
    private String username;
    private String displayName;
    private String profilePicture;
    private String role;
    private Long artistProfileId;
    private String token;
    private String message;
    private boolean authenticated;

    public static AuthResponse success(Long userId, String email, String username, String role, String token) {
        return AuthResponse.builder()
                .userId(userId)
                .email(email)
                .username(username)
                .role(role)
                .token(token)
                .message("Login successful")
                .authenticated(true)
                .build();
    }

    public static AuthResponse failure(String message) {
        return AuthResponse.builder()
                .message(message)
                .authenticated(false)
                .build();
    }
}
