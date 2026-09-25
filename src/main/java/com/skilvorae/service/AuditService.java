package com.skilvorae.service;

import com.skilvorae.entity.AuditLog;

import java.util.List;

/**
 * Service interface for auditing administrative actions, course updates, and user logins on SkilVorae.
 */
public interface AuditService {

    /**
     * Logs an operational audit log.
     *
     * @param userId Associated User ID.
     * @param action Event action string.
     * @param details Event description.
     */
    void logAction(Long userId, String action, String details);

    /**
     * Retrieves audit logs for admin review.
     *
     * @return List of AuditLog entities.
     */
    List<AuditLog> getRecentAuditLogs();
}
