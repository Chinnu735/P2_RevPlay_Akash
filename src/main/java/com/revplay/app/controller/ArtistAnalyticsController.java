package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.ArtistAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class ArtistAnalyticsController {

    private final ArtistAnalyticsService analyticsService;

    // ── ARTIST DASHBOARD ─────────────────────────────────────────────────
    @GetMapping("/artist/{artistId}/dashboard")
    public ResponseEntity<ArtistDashboardResponse> getDashboard(@PathVariable Long artistId) {
        return ResponseEntity.ok(analyticsService.getDashboard(artistId));
    }

    // ── PLAY COUNT FOR A SPECIFIC SONG ───────────────────────────────────
    @GetMapping("/song/{songId}/plays")
    public ResponseEntity<Map<String, Object>> getSongPlayCount(@PathVariable Long songId) {
        long count = analyticsService.getPlayCountForSong(songId);
        return ResponseEntity.ok(Map.of("songId", songId, "playCount", count));
    }

    // ── SONGS SORTED BY POPULARITY ───────────────────────────────────────
    @GetMapping("/artist/{artistId}/songs/popular")
    public ResponseEntity<List<ArtistDashboardResponse.SongPlayCount>> getPopularSongs(
            @PathVariable Long artistId) {
        return ResponseEntity.ok(analyticsService.getSongsByPopularity(artistId));
    }

    // ── USERS WHO FAVORITED ARTIST'S SONGS ───────────────────────────────
    @GetMapping("/artist/{artistId}/favorites/users")
    public ResponseEntity<List<Map<String, Object>>> getFavoritedByUsers(
            @PathVariable Long artistId) {
        return ResponseEntity.ok(analyticsService.getUsersWhoFavoritedArtistSongs(artistId));
    }

    // ── DAILY PLAY TRENDS ────────────────────────────────────────────────
    @GetMapping("/artist/{artistId}/trends")
    public ResponseEntity<List<Map<String, Object>>> getDailyTrends(
            @PathVariable Long artistId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return ResponseEntity.ok(analyticsService.getDailyPlayTrends(artistId, start, end));
    }

    // ── TOP LISTENERS ────────────────────────────────────────────────────
    @GetMapping("/artist/{artistId}/listeners/top")
    public ResponseEntity<List<ArtistDashboardResponse.TopListener>> getTopListeners(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getTopListeners(artistId, limit));
    }

    // ── COMBINED ARTIST PAGE (artist + songs + albums) ───────────────────
    @GetMapping("/artist/{artistId}/page")
    public ResponseEntity<ArtistPageResponse> getArtistPage(@PathVariable Long artistId) {
        return ResponseEntity.ok(analyticsService.getArtistPage(artistId));
    }

    // ── COMBINED ALBUM DETAIL (album + track list) ───────────────────────
    @GetMapping("/album/{albumId}/detail")
    public ResponseEntity<AlbumDetailResponse> getAlbumDetail(@PathVariable Long albumId) {
        return ResponseEntity.ok(analyticsService.getAlbumDetail(albumId));
    }

    // ── USER STATISTICS ──────────────────────────────────────────────────
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<UserStatsResponse> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.getUserStats(userId));
    }
}
