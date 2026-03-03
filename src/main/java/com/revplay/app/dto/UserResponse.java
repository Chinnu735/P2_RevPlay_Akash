package com.revplay.app.dto;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String role;
    private String displayName;
    private String bio;
    private String profilePicture;
    private String securityQuestion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isActive;

    public UserResponse() {
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public static class UserResponseBuilder {
        private UserResponse response = new UserResponse();

        public UserResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public UserResponseBuilder email(String email) {
            response.setEmail(email);
            return this;
        }

        public UserResponseBuilder username(String username) {
            response.setUsername(username);
            return this;
        }

        public UserResponseBuilder role(String role) {
            response.setRole(role);
            return this;
        }

        public UserResponseBuilder displayName(String displayName) {
            response.setDisplayName(displayName);
            return this;
        }

        public UserResponseBuilder bio(String bio) {
            response.setBio(bio);
            return this;
        }

        public UserResponseBuilder profilePicture(String profilePicture) {
            response.setProfilePicture(profilePicture);
            return this;
        }

        public UserResponseBuilder securityQuestion(String securityQuestion) {
            response.setSecurityQuestion(securityQuestion);
            return this;
        }

        public UserResponseBuilder createdAt(LocalDateTime createdAt) {
            response.setCreatedAt(createdAt);
            return this;
        }

        public UserResponseBuilder updatedAt(LocalDateTime updatedAt) {
            response.setUpdatedAt(updatedAt);
            return this;
        }

        public UserResponseBuilder isActive(Integer isActive) {
            response.setIsActive(isActive);
            return this;
        }

        public UserResponse build() {
            return response;
        }
    }
}
