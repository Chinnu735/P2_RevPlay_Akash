package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.IPlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private static final Logger log = LoggerFactory.getLogger(PlaylistController.class);

    private final IPlaylistService playlistService;

    @PostMapping
    public ResponseEntity<PlaylistResponse> create(@Valid @RequestBody PlaylistRequest request) {
        log.info("POST /api/playlists - name: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(playlistService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> getAll() {
        return ResponseEntity.ok(playlistService.getAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistResponse>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(playlistService.getByUserId(userId));
    }

    @GetMapping("/public")
    public ResponseEntity<List<PlaylistResponse>> getPublicPlaylists() {
        return ResponseEntity.ok(playlistService.getPublicPlaylists());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistResponse> update(@PathVariable Long id, @Valid @RequestBody PlaylistRequest request) {
        return ResponseEntity.ok(playlistService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playlistService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
