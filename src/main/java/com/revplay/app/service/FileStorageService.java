package com.revplay.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> SONG_EXTENSIONS = Set.of(
            ".mp3", ".wav", ".ogg", ".m4a", ".aac");

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".webp");

    private static final long MAX_SIZE = 100 * 1024 * 1024; // 100MB

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadPath) {
        this.uploadRoot = Paths.get(uploadPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + uploadPath, e);
        }
    }

    public String store(MultipartFile file, String subDir) throws IOException {
        // Validate not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate size
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File exceeds maximum size of 50MB");
        }

        // Validate extension
        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
        }

        boolean isSong = SONG_EXTENSIONS.contains(extension);
        boolean isImage = IMAGE_EXTENSIONS.contains(extension);

        if (!isSong && !isImage) {
            throw new IllegalArgumentException("Unsupported file type");
        }

        // Generate unique filename: UUID + "-" + originalName
        // Sanitize originalName just in case
        String sanitizedName = originalName != null ? originalName.replaceAll("[^a-zA-Z0-9._-]", "_") : "file";
        String uniqueName = UUID.randomUUID().toString() + "-" + sanitizedName;

        Path targetPath;
        if (isImage) {
            // For images, store directly in uploadRoot as requested
            targetPath = this.uploadRoot.resolve(uniqueName);
        } else {
            // For songs, keep subDir if provided, else root
            Path targetDir = subDir != null ? this.uploadRoot.resolve(subDir) : this.uploadRoot;
            Files.createDirectories(targetDir);
            targetPath = targetDir.resolve(uniqueName);
        }

        // Copy file
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Return ONLY the filename (or relative path if in subDir)
        if (isImage) {
            return uniqueName;
        } else {
            return (subDir != null ? subDir + "/" : "") + uniqueName;
        }
    }

    public String store(MultipartFile file) throws IOException {
        String ext = "";
        String on = file.getOriginalFilename();
        if (on != null && on.contains(".")) {
            ext = on.substring(on.lastIndexOf('.')).toLowerCase();
        }
        String subDir = SONG_EXTENSIONS.contains(ext) ? "songs" : null;
        return store(file, subDir);
    }
}
