package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.PodcastEpisodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/podcast-episodes")
@RequiredArgsConstructor
public class PodcastEpisodeController {

    private final PodcastEpisodeService episodeService;

    @PostMapping
    public ResponseEntity<PodcastEpisodeResponse> create(@Valid @RequestBody PodcastEpisodeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(episodeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PodcastEpisodeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.getById(id));
    }

    @GetMapping("/podcast/{podcastId}")
    public ResponseEntity<List<PodcastEpisodeResponse>> getByPodcastId(@PathVariable Long podcastId) {
        return ResponseEntity.ok(episodeService.getByPodcastId(podcastId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PodcastEpisodeResponse> update(@PathVariable Long id,
            @Valid @RequestBody PodcastEpisodeRequest request) {
        return ResponseEntity.ok(episodeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        episodeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
