package com.revplay.app.dto;

public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private Long userId;

    public AuthResponse() {
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static class AuthResponseBuilder {
        private AuthResponse response = new AuthResponse();

        public AuthResponseBuilder token(String token) {
            response.setToken(token);
            return this;
        }

        public AuthResponseBuilder email(String email) {
            response.setEmail(email);
            return this;
        }

        public AuthResponseBuilder role(String role) {
            response.setRole(role);
            return this;
        }

        public AuthResponseBuilder userId(Long userId) {
            response.setUserId(userId);
            return this;
        }

        public AuthResponse build() {
            return response;
        }
    }
}
