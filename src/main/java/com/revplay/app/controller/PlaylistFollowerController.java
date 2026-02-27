package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.PlaylistFollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist-followers")
@RequiredArgsConstructor
public class PlaylistFollowerController {

    private final PlaylistFollowerService followerService;

    @PostMapping
    public ResponseEntity<PlaylistFollowerResponse> follow(@Valid @RequestBody PlaylistFollowerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(followerService.followPlaylist(request));
    }

    @DeleteMapping("/playlist/{playlistId}/user/{userId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long playlistId, @PathVariable Long userId) {
        followerService.unfollowPlaylist(playlistId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<PlaylistFollowerResponse>> getFollowers(@PathVariable Long playlistId) {
        return ResponseEntity.ok(followerService.getFollowersByPlaylistId(playlistId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistFollowerResponse>> getFollowedPlaylists(@PathVariable Long userId) {
        return ResponseEntity.ok(followerService.getFollowedPlaylistsByUserId(userId));
    }
}
