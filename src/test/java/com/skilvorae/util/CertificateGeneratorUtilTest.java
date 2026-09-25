package com.skilvorae.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for CertificateGeneratorUtil in SkilVorae backend.
 */
public class CertificateGeneratorUtilTest {

    @Test
    @DisplayName("Should generate valid unique certificate code formatted with prefix")
    void generateCertificateCode_Success() {
        String code = CertificateGeneratorUtil.generateCertificateCode(10L, 101L);

        assertNotNull(code);
        assertTrue(code.startsWith("SKV-"));
        assertTrue(code.contains("-10-101-"));
    }

    @Test
    @DisplayName("Should build valid PDF binary representation for course completion certificate")
    void generatePdfCertificate_Success() {
        byte[] pdf = CertificateGeneratorUtil.generatePdfCertificate(
                "Alex Morgan",
                "Full Stack Spring Boot & React",
                "Dr. Alan Turing",
                "SKV-2026-10-101-998877",
                LocalDateTime.now()
        );

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        String pdfHeader = new String(pdf);
        assertTrue(pdfHeader.startsWith("%PDF"));
    }
}
