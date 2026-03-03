package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.IAlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    private static final Logger log = LoggerFactory.getLogger(AlbumController.class);

    private final IAlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumResponse> create(@Valid @RequestBody AlbumRequest request) {
        log.info("POST /api/albums - name: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> getById(@PathVariable Long id) {
        log.info("GET /api/albums/{}", id);
        return ResponseEntity.ok(albumService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponse>> getAll() {
        log.info("GET /api/albums");
        return ResponseEntity.ok(albumService.getAll());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<AlbumResponse>> getByArtistId(@PathVariable Long artistId) {
        return ResponseEntity.ok(albumService.getByArtistId(artistId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> update(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        return ResponseEntity.ok(albumService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        albumService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
