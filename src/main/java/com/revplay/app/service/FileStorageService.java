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

    private static final long MAX_SIZE = 50 * 1024 * 1024; // 50MB

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

        // Ensure subDir exists
        Path targetDir = this.uploadRoot.resolve(subDir);
        Files.createDirectories(targetDir);

        // Generate unique filename
        String uniqueName = UUID.randomUUID().toString() + extension;
        Path targetPath = targetDir.resolve(uniqueName);

        // Copy file
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Return the URL path
        return "/uploads/" + subDir + "/" + uniqueName;
    }

    public String store(MultipartFile file) throws IOException {
        return store(file, "songs");
    }
}
