package com.skilvorae.service.impl;

import com.skilvorae.exception.BadRequestException;
import com.skilvorae.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Enterprise implementation of FileStorageService for SkilVorae managing course thumbnails, video uploads, and avatars.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded file cannot be empty.");
        }

        try {
            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String filename = UUID.randomUUID().toString() + ext;
            Path target = Paths.get(uploadDir, folder, filename);
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + folder + "/" + filename;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store upload file.", ex);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        // Implementation for file deletion
    }
}
