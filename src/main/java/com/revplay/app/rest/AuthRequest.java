package com.revplay.app.rest;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    private String email;
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
}
