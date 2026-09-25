package com.skilvorae.service;

import com.skilvorae.entity.AuditLog;
import com.skilvorae.repository.AuditLogRepository;
import com.skilvorae.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 unit test suite for AuditServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    @DisplayName("Should persist security audit log entry to repository")
    void logEvent_Success() {
        auditService.logEvent("USER_LOGIN", "student@skilvorae.com", "User authenticated successfully from 127.0.0.1");

        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }
}
