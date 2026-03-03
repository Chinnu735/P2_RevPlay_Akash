package com.revplay.app.controller;

import com.revplay.app.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private final FileStorageService fileStorageService;

    @PostMapping("/song")
    public ResponseEntity<?> uploadSong(@RequestParam("file") MultipartFile file) {
        try {
            // Store song (store() will return "songs/filename" or similar)
            String path = fileStorageService.store(file, "songs");
            return ResponseEntity.ok(Map.of(
                    "url", "/uploads/" + path, // Prefix with /uploads/ for the frontend
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            log.error("Failed to store song", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to store file"));
        }
    }

    @PostMapping("/profile-photo")
    public ResponseEntity<?> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        try {
            // store() returns just the filename for images
            String filename = fileStorageService.store(file, null);
            return ResponseEntity.ok(Map.of(
                    "url", filename, // Return only filename as requested
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            log.error("Failed to store profile photo", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to store file"));
        }
    }

    @PostMapping("/banner")
    public ResponseEntity<?> uploadBanner(@RequestParam("file") MultipartFile file) {
        try {
            String filename = fileStorageService.store(file, null);
            return ResponseEntity.ok(Map.of(
                    "url", filename,
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            log.error("Failed to store banner", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to store file"));
        }
    }

    @PostMapping("/podcast")
    public ResponseEntity<?> uploadPodcast(@RequestParam("file") MultipartFile file) {
        try {
            // Store podcast episode in 'podcasts' subDir
            String path = fileStorageService.store(file, "podcasts");
            return ResponseEntity.ok(Map.of(
                    "url", "/uploads/" + path,
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            log.error("Failed to store podcast", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to store file"));
        }
    }
}
