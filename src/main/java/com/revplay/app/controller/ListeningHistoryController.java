package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.ListeningHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listening-history")
@RequiredArgsConstructor
public class ListeningHistoryController {

    private final ListeningHistoryService historyService;

    @PostMapping
    public ResponseEntity<ListeningHistoryResponse> recordPlay(@Valid @RequestBody ListeningHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.recordPlay(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ListeningHistoryResponse>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getHistoryByUserId(userId));
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<ListeningHistoryResponse>> getRecentHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(historyService.getRecentHistory(userId, limit));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearHistory(@PathVariable Long userId) {
        historyService.clearHistory(userId);
        return ResponseEntity.noContent().build();
    }
}
