package com.skilvorae.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite verifying AES-256-GCM encryption and decryption in SkilVorae backend.
 */
public class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        ReflectionTestUtils.setField(encryptionService, "secretKeyPassphrase", "SkilVoraeTestEncryptionSecretKey2026!");
    }

    @Test
    @DisplayName("Should encrypt raw string and decrypt back to exact content")
    void encryptAndDecrypt_Success() {
        String sensitive = "SkilVorae Secret Payment Token: 9876-5432-1098-7654";

        String encrypted = encryptionService.encrypt(sensitive);
        assertNotNull(encrypted);
        assertNotEquals(sensitive, encrypted);

        String decrypted = encryptionService.decrypt(encrypted);
        assertEquals(sensitive, decrypted);
    }
}
