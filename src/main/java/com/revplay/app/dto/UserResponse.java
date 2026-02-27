package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
