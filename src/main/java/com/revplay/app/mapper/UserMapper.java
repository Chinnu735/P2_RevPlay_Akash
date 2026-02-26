package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .displayName(user.getDisplayName())
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .securityQuestion(user.getSecurityQuestion())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isActive(user.getIsActive())
                .build();
    }

    public User toEntity(UserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setDisplayName(request.getDisplayName());
        user.setBio(request.getBio());
        user.setProfilePicture(request.getProfilePicture());
        user.setSecurityQuestion(request.getSecurityQuestion());
        user.setSecurityAnswer(request.getSecurityAnswer());
        user.setIsActive(1);
        return user;
    }

    public void updateEntity(User user, UserRequest request) {
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }
        user.setRole(request.getRole());
        user.setDisplayName(request.getDisplayName());
        user.setBio(request.getBio());
        user.setProfilePicture(request.getProfilePicture());
        user.setSecurityQuestion(request.getSecurityQuestion());
        if (request.getSecurityAnswer() != null && !request.getSecurityAnswer().isBlank()) {
            user.setSecurityAnswer(request.getSecurityAnswer());
        }
    }
}
