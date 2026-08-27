package com.skilvorae.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String saveFile(MultipartFile file, String subDirectory) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }

            String newFileName = UUID.randomUUID().toString() + fileExtension;
            
            Path targetDir = this.fileStorageLocation.resolve(subDirectory).normalize();
            Files.createDirectories(targetDir);
            
            Path targetLocation = targetDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + subDirectory + "/" + newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty() || !fileUrl.startsWith("/uploads/")) {
            return;
        }
        
        String relativePath = fileUrl.substring("/uploads/".length());
        Path targetLocation = this.fileStorageLocation.resolve(relativePath).normalize();
        
        // Security check to ensure the file is within the intended directory
        if (!targetLocation.startsWith(this.fileStorageLocation)) {
            return;
        }

        try {
            Files.deleteIfExists(targetLocation);
        } catch (IOException ex) {
            System.err.println("Failed to delete file: " + targetLocation + ". Error: " + ex.getMessage());
        }
    }
}
