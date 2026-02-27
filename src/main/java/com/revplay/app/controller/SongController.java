package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping
    public ResponseEntity<SongResponse> create(@Valid @RequestBody SongRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SongResponse>> getAll() {
        return ResponseEntity.ok(songService.getAll());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<SongResponse>> getByArtistId(@PathVariable Long artistId) {
        return ResponseEntity.ok(songService.getByArtistId(artistId));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<SongResponse>> getByAlbumId(@PathVariable Long albumId) {
        return ResponseEntity.ok(songService.getByAlbumId(albumId));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<SongResponse>> getByGenreId(@PathVariable Long genreId) {
        return ResponseEntity.ok(songService.getByGenreId(genreId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SongResponse>> search(@RequestParam String title) {
        return ResponseEntity.ok(songService.search(title));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<SongResponse>> getTrending() {
        return ResponseEntity.ok(songService.getTrendingSongs());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<SongResponse>> getRecent() {
        return ResponseEntity.ok(songService.getRecentSongs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongResponse> update(@PathVariable Long id, @Valid @RequestBody SongRequest request) {
        return ResponseEntity.ok(songService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        songService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
