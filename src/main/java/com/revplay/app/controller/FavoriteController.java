package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(@Valid @RequestBody FavoriteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.addFavorite(request));
    }

    @DeleteMapping("/user/{userId}/song/{songId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId, @PathVariable Long songId) {
        favoriteService.removeFavorite(userId, songId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUserId(userId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorite(@RequestParam Long userId, @RequestParam Long songId) {
        return ResponseEntity.ok(favoriteService.isFavorite(userId, songId));
    }
}
