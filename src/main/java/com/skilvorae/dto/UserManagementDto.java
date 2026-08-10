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
}
