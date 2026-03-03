package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.service.IGenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private static final Logger log = LoggerFactory.getLogger(GenreController.class);

    private final IGenreService genreService;

    @PostMapping
    public ResponseEntity<GenreResponse> create(@Valid @RequestBody GenreRequest request) {
        log.info("POST /api/genres - name: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<GenreResponse>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> update(@PathVariable Long id, @Valid @RequestBody GenreRequest request) {
        return ResponseEntity.ok(genreService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
