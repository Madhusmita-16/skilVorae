package com.skilvorae.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Enterprise AES-256-GCM encryption service for securing PII, student credentials,
 * payment token metadata, and certificate signatures at rest for SkilVorae.
 */
@Component
public class EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH_BYTES = 12;
    private static final int SALT_LENGTH_BYTES = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH_BITS = 256;

    @Value("${app.security.secret-key:SkilVoraeSecureEdTechPlatformEncryptionSecretKey2026!}")
    private String secretKeyPassphrase;

    /**
     * Encrypts plain text string using AES-256-GCM authenticated encryption.
     *
     * @param plainText Raw string to encrypt.
     * @return Base64-encoded encrypted string payload.
     */
    public String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }

        try {
            byte[] salt = new byte[SALT_LENGTH_BYTES];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            byte[] iv = new byte[IV_LENGTH_BYTES];
            random.nextBytes(iv);

            SecretKey secretKey = deriveKey(secretKeyPassphrase, salt);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[salt.length + iv.length + cipherText.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(iv, 0, combined, salt.length, iv.length);
            System.arraycopy(cipherText, 0, combined, salt.length + iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception ex) {
            throw new RuntimeException("Encryption failure while securing SkilVorae user data.", ex);
        }
    }

    /**
     * Decrypts Base64-encoded AES-256-GCM payload back to plain text.
     *
     * @param encryptedBase64 Base64 string payload.
     * @return Decrypted plain text string.
     */
    public String decrypt(String encryptedBase64) {
        if (encryptedBase64 == null || encryptedBase64.isEmpty()) {
            return encryptedBase64;
        }

        try {
            byte[] combined = Base64.getDecoder().decode(encryptedBase64);

            byte[] salt = new byte[SALT_LENGTH_BYTES];
            byte[] iv = new byte[IV_LENGTH_BYTES];
            byte[] cipherText = new byte[combined.length - SALT_LENGTH_BYTES - IV_LENGTH_BYTES];

            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH_BYTES);
            System.arraycopy(combined, SALT_LENGTH_BYTES, iv, 0, IV_LENGTH_BYTES);
            System.arraycopy(combined, SALT_LENGTH_BYTES + IV_LENGTH_BYTES, cipherText, 0, cipherText.length);

            SecretKey secretKey = deriveKey(secretKeyPassphrase, salt);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] plainTextBytes = cipher.doFinal(cipherText);
            return new String(plainTextBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("Decryption failure while retrieving user data.", ex);
        }
    }

    private SecretKey deriveKey(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH_BITS);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}
