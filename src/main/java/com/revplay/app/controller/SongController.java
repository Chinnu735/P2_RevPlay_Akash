package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.ISongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {
    private static final Logger log = LoggerFactory.getLogger(SongController.class);

    private final ISongService songService;

    @PostMapping
    public ResponseEntity<SongResponse> create(@Valid @RequestBody SongRequest request) {
        log.info("POST /api/songs - title: {}", request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> getById(@PathVariable Long id) {
        log.info("GET /api/songs/{}", id);
        return ResponseEntity.ok(songService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SongResponse>> getAll() {
        log.info("GET /api/songs");
        return ResponseEntity.ok(songService.getAll());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<SongResponse>> getByArtistId(@PathVariable Long artistId) {
        log.info("GET /api/songs/artist/{}", artistId);
        return ResponseEntity.ok(songService.getByArtistId(artistId));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<SongResponse>> getByAlbumId(@PathVariable Long albumId) {
        log.info("GET /api/songs/album/{}", albumId);
        return ResponseEntity.ok(songService.getByAlbumId(albumId));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<SongResponse>> getByGenreId(@PathVariable Long genreId) {
        log.info("GET /api/songs/genre/{}", genreId);
        return ResponseEntity.ok(songService.getByGenreId(genreId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SongResponse>> search(@RequestParam String title) {
        log.info("GET /api/songs/search?title={}", title);
        return ResponseEntity.ok(songService.search(title));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<SongResponse>> getTrending() {
        log.info("GET /api/songs/trending");
        return ResponseEntity.ok(songService.getTrendingSongs());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<SongResponse>> getRecent() {
        log.info("GET /api/songs/recent");
        return ResponseEntity.ok(songService.getRecentSongs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongResponse> update(@PathVariable Long id, @Valid @RequestBody SongRequest request) {
        log.info("PUT /api/songs/{} - title: {}", id, request.getTitle());
        return ResponseEntity.ok(songService.update(id, request));
    }

    @PutMapping("/{id}/duration")
    public ResponseEntity<Void> updateDuration(@PathVariable Long id, @RequestParam Integer duration) {
        log.info("PUT /api/songs/{}/duration - duration: {}", id, duration);
        songService.updateDuration(id, duration);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        songService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
