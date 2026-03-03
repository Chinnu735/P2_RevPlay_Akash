package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.IPlaylistSongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist-songs")
@RequiredArgsConstructor
public class PlaylistSongController {
    private static final Logger log = LoggerFactory.getLogger(PlaylistSongController.class);

    private final IPlaylistSongService playlistSongService;

    @PostMapping
    public ResponseEntity<PlaylistSongResponse> addSong(@Valid @RequestBody PlaylistSongRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playlistSongService.addSongToPlaylist(request));
    }

    @DeleteMapping("/playlist/{playlistId}/song/{songId}")
    public ResponseEntity<Void> removeSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistSongService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<PlaylistSongResponse>> getSongsByPlaylist(@PathVariable Long playlistId) {
        return ResponseEntity.ok(playlistSongService.getSongsByPlaylistId(playlistId));
    }

    @PutMapping("/playlist/{playlistId}/song/{songId}/reorder")
    public ResponseEntity<Void> reorderSong(@PathVariable Long playlistId,
            @PathVariable Long songId,
            @RequestParam Integer newOrderIndex) {
        playlistSongService.reorderSong(playlistId, songId, newOrderIndex);
        return ResponseEntity.ok().build();
    }
}
