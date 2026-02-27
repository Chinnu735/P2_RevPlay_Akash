package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.ArtistProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistProfileController {

    private final ArtistProfileService artistProfileService;

    @PostMapping
    public ResponseEntity<ArtistProfileResponse> create(@Valid @RequestBody ArtistProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistProfileService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistProfileResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(artistProfileService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ArtistProfileResponse> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(artistProfileService.getByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<ArtistProfileResponse>> getAll() {
        return ResponseEntity.ok(artistProfileService.getAll());
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<ArtistProfileResponse>> getByGenreId(@PathVariable Long genreId) {
        return ResponseEntity.ok(artistProfileService.getByGenreId(genreId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistProfileResponse> update(@PathVariable Long id,
            @Valid @RequestBody ArtistProfileRequest request) {
        return ResponseEntity.ok(artistProfileService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        artistProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
