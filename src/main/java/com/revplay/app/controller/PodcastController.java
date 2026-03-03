package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.IPodcastService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/podcasts")
@RequiredArgsConstructor
public class PodcastController {
    private static final Logger log = LoggerFactory.getLogger(PodcastController.class);

    private final IPodcastService podcastService;

    @PostMapping
    public ResponseEntity<PodcastResponse> create(@Valid @RequestBody PodcastRequest request) {
        log.info("POST /api/podcasts - title: {}", request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(podcastService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PodcastResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(podcastService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PodcastResponse>> getAll() {
        log.info("GET /api/podcasts");
        return ResponseEntity.ok(podcastService.getAll());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<PodcastResponse>> getByArtistId(@PathVariable Long artistId) {
        return ResponseEntity.ok(podcastService.getByArtistId(artistId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PodcastResponse> update(@PathVariable Long id, @Valid @RequestBody PodcastRequest request) {
        return ResponseEntity.ok(podcastService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        podcastService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
