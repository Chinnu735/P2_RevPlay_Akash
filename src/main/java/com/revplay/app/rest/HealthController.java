package com.revplay.app.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "RevPlay",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().toString()));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "name", "RevPlay Music Streaming API",
                "version", "1.0.0",
                "description", "REST API for music streaming platform",
                "endpoints", Map.of(
                        "genres", "/api/genres",
                        "users", "/api/users",
                        "artists", "/api/artists",
                        "albums", "/api/albums",
                        "songs", "/api/songs",
                        "playlists", "/api/playlists",
                        "podcasts", "/api/podcasts",
                        "podcastEpisodes", "/api/podcast-episodes",
                        "favorites", "/api/favorites",
                        "listeningHistory", "/api/listening-history")));
    }
}
