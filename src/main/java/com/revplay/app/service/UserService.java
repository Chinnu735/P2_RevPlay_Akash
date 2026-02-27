package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);

    UserResponse getById(Long id);

    UserResponse getByEmail(String email);

    UserResponse getByUsername(String username);

    List<UserResponse> getAll();

    UserResponse update(Long id, UserRequest request);

    void resetPassword(PasswordResetRequest request);

    void delete(Long id);
}
