package com.revplay.app.controller;

import com.revplay.app.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/song")
    public ResponseEntity<?> uploadSong(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileStorageService.store(file, "songs");
            return ResponseEntity.ok(Map.of(
                    "url", url,
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to store file"));
        }
    }

    @PostMapping("/profile-photo")
    public ResponseEntity<?> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileStorageService.store(file, "profiles");
            return ResponseEntity.ok(Map.of(
                    "url", url,
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to store file"));
        }
    }
}
