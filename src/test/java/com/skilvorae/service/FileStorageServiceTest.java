package com.skilvorae.service;

import com.skilvorae.service.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 unit test suite for FileStorageServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageServiceImpl();
    }

    @Test
    @DisplayName("Should store uploaded file and return relative file URL path")
    void storeFile_Success() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "sample-assignment.pdf",
                "application/pdf",
                "Sample PDF Content".getBytes()
        );

        String filePath = fileStorageService.storeFile(mockFile, "assignments");

        assertNotNull(filePath);
        assertTrue(filePath.contains("assignments"));
        assertTrue(filePath.endsWith(".pdf"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when storing empty file")
    void storeFile_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        assertThrows(IllegalArgumentException.class, () -> fileStorageService.storeFile(emptyFile, "temp"));
    }
}
