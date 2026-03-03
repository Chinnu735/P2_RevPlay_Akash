package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.User;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.UserMapper;
import com.revplay.app.repository.IUserRepository;
import com.revplay.app.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse create(UserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Duplicate email registration attempt: {}", request.getEmail());
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Duplicate username registration attempt: {}", request.getUsername());
            throw new DuplicateResourceException("Username already taken: " + request.getUsername());
        }
        User user = userMapper.toEntity(request);
        UserResponse response = userMapper.toResponse(userRepository.save(user));
        log.info("User created successfully with id: {}", response.getId());
        return response;
    }

    @Override
    public UserResponse getById(Long id) {
        log.debug("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        log.info("Updating user id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        // If a new password is provided, verify the current password first
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
                throw new IllegalArgumentException("Current password is required to set a new password");
            }
            if (!user.getPassword().equals(request.getCurrentPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
        }

        userMapper.updateEntity(user, request);
        UserResponse response = userMapper.toResponse(userRepository.save(user));
        log.info("User updated successfully: {}", id);
        return response;
    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (user.getSecurityAnswer() == null
                || !user.getSecurityAnswer().equalsIgnoreCase(request.getSecurityAnswer().trim())) {
            log.warn("Password reset failed - incorrect security answer for email: {}", request.getEmail());
            throw new IllegalArgumentException("Incorrect security answer");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        log.info("Password reset successful for email: {}", request.getEmail());
    }

    @Override
    public void delete(Long id) {
        log.info("Soft-deleting user id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setIsActive(0);
        userRepository.save(user);
        log.info("User soft-deleted successfully: {}", id);
    }
}
