package com.revplay.app.rest;

import com.revplay.app.entity.ArtistProfile;
import com.revplay.app.entity.Genre;
import com.revplay.app.entity.User;
import com.revplay.app.repository.IArtistProfileRepository;
import com.revplay.app.repository.IGenreRepository;
import com.revplay.app.repository.IUserRepository;
import com.revplay.app.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserRepository userRepository;
    private final IArtistProfileRepository artistProfileRepository;
    private final IGenreRepository genreRepository;
    private final JwtService jwtService;

    // ── LOGIN (supports email OR username) ───────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Optional<User> optionalUser;

        // Try email first, then username
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            optionalUser = userRepository.findByEmail(request.getEmail());
        } else if (request.getUsername() != null && !request.getUsername().isBlank()) {
            optionalUser = userRepository.findByUsername(request.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.failure("Email or username is required"));
        }

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.failure("Invalid credentials"));
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.failure("Invalid credentials"));
        }

        if (user.getIsActive() == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(AuthResponse.failure("Account is deactivated"));
        }

        // If artist, include artistProfileId in response
        Long artistProfileId = null;
        if ("artist".equalsIgnoreCase(user.getRole())) {
            Optional<ArtistProfile> profile = artistProfileRepository.findByUserId(user.getId());
            artistProfileId = profile.map(ArtistProfile::getId).orElse(null);
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getId(), user.getRole());

        return ResponseEntity.ok(AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole())
                .token(token)
                .artistProfileId(artistProfileId)
                .message("Login successful")
                .authenticated(true)
                .build());
    }

    // ── REGISTER (regular user) ──────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Email already registered"));
        }

        String username = (request.getUsername() != null && !request.getUsername().isBlank())
                ? request.getUsername()
                : request.getEmail().split("@")[0];

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Username already taken"));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUsername(username);
        user.setRole("user");
        user.setSecurityQuestion(request.getSecurityQuestion());
        user.setSecurityAnswer(request.getSecurityAnswer());
        user.setIsActive(1);

        User saved = userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(saved.getId(), saved.getRole());

        AuthResponse authResponse = AuthResponse.success(
                saved.getId(), saved.getEmail(), saved.getUsername(), saved.getRole(), token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(authResponse));
    }

    // ── REGISTER ARTIST (User + ArtistProfile in one call) ───────────────
    @PostMapping("/register/artist")
    public ResponseEntity<ApiResponse<AuthResponse>> registerArtist(
            @RequestBody ArtistRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Email already registered"));
        }

        String username = (request.getUsername() != null && !request.getUsername().isBlank())
                ? request.getUsername()
                : request.getEmail().split("@")[0];

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Username already taken"));
        }

        // Create user with artist role
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUsername(username);
        user.setRole("artist");
        user.setSecurityQuestion(request.getSecurityQuestion());
        user.setSecurityAnswer(request.getSecurityAnswer());
        user.setIsActive(1);
        User savedUser = userRepository.save(user);

        // Create artist profile
        ArtistProfile profile = new ArtistProfile();
        profile.setUser(savedUser);
        profile.setArtistName(request.getArtistName() != null ? request.getArtistName() : username);
        profile.setInstagramLink(request.getInstagramLink());
        profile.setYoutubeLink(request.getYoutubeLink());
        profile.setBannerImage(request.getBannerImage());

        if (request.getGenreId() != null) {
            Genre genre = genreRepository.findById(request.getGenreId()).orElse(null);
            profile.setGenre(genre);
        }

        ArtistProfile savedProfile = artistProfileRepository.save(profile);

        // Generate JWT token
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getRole());

        AuthResponse authResponse = AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .token(token)
                .artistProfileId(savedProfile.getId())
                .message("Artist registration successful")
                .authenticated(true)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(authResponse));
    }
}
