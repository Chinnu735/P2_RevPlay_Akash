package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be 3-100 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required")
    @Size(max = 20)
    private String role;

    @Size(max = 100)
    private String displayName;

    private String bio;

    @Size(max = 500)
    private String profilePicture;

    @Size(max = 255)
    private String securityQuestion;

    @Size(max = 255)
    private String securityAnswer;
}
