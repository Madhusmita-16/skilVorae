package com.skilvorae.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDto {
    private Long id;
    private String userEmail;
    private String action;
    private String entityType;
    private Long entityId;
    private String details;
    private String formattedTimestamp;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getFormattedTimestamp() { return formattedTimestamp; }
    public void setFormattedTimestamp(String formattedTimestamp) { this.formattedTimestamp = formattedTimestamp; }

    public static AuditLogDtoBuilder builder() { return new AuditLogDtoBuilder(); }

    public static class AuditLogDtoBuilder {
        private Long id;
        private String userEmail;
        private String action;
        private String entityType;
        private Long entityId;
        private String details;
        private String formattedTimestamp;

        public AuditLogDtoBuilder id(Long id) { this.id = id; return this; }
        public AuditLogDtoBuilder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public AuditLogDtoBuilder action(String action) { this.action = action; return this; }
        public AuditLogDtoBuilder entityType(String entityType) { this.entityType = entityType; return this; }
        public AuditLogDtoBuilder entityId(Long entityId) { this.entityId = entityId; return this; }
        public AuditLogDtoBuilder details(String details) { this.details = details; return this; }
        public AuditLogDtoBuilder formattedTimestamp(String formattedTimestamp) { this.formattedTimestamp = formattedTimestamp; return this; }

        public AuditLogDto build() {
            return new AuditLogDto(id, userEmail, action, entityType, entityId, details, formattedTimestamp);
        }
    }
}
