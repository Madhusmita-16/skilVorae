package com.skilvorae.service;

import com.skilvorae.service.impl.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for MailServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    @DisplayName("Should send welcome email notification to newly registered user")
    void sendWelcomeEmail_Success() {
        mailService.sendWelcomeEmail("student@skilvorae.com", "Alex");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send certificate completion email notification to student")
    void sendCertificateEmail_Success() {
        mailService.sendCertificateEmail("student@skilvorae.com", "Alex", "Java Enterprise 17", "SKV-2026-CERT-01");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
