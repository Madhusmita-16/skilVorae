package com.skilvorae.dto;

import com.skilvorae.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementDto {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private boolean active;
    private String joinedDate;
    private int enrolledCoursesCount;
    private String lastActive;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getJoinedDate() { return joinedDate; }
    public void setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }
    public int getEnrolledCoursesCount() { return enrolledCoursesCount; }
    public void setEnrolledCoursesCount(int enrolledCoursesCount) { this.enrolledCoursesCount = enrolledCoursesCount; }
    public String getLastActive() { return lastActive; }
    public void setLastActive(String lastActive) { this.lastActive = lastActive; }

    public static UserManagementDtoBuilder builder() { return new UserManagementDtoBuilder(); }

    public static class UserManagementDtoBuilder {
        private Long id;
        private String fullName;
        private String email;
        private Role role;
        private boolean active;
        private String joinedDate;
        private int enrolledCoursesCount;
        private String lastActive;

        public UserManagementDtoBuilder id(Long id) { this.id = id; return this; }
        public UserManagementDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserManagementDtoBuilder email(String email) { this.email = email; return this; }
        public UserManagementDtoBuilder role(Role role) { this.role = role; return this; }
        public UserManagementDtoBuilder active(boolean active) { this.active = active; return this; }
        public UserManagementDtoBuilder joinedDate(String joinedDate) { this.joinedDate = joinedDate; return this; }
        public UserManagementDtoBuilder enrolledCoursesCount(int enrolledCoursesCount) { this.enrolledCoursesCount = enrolledCoursesCount; return this; }
        public UserManagementDtoBuilder lastActive(String lastActive) { this.lastActive = lastActive; return this; }

        public UserManagementDto build() {
            return new UserManagementDto(id, fullName, email, role, active, joinedDate, enrolledCoursesCount, lastActive);
        }
    }
}
