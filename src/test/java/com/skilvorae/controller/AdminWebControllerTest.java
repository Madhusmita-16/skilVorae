package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.dto.UserDto;
import com.skilvorae.enums.Role;
import com.skilvorae.service.CourseService;
import com.skilvorae.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for AdminWebController in SkilVorae backend.
 */
@WebMvcTest(AdminWebController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private CourseService courseService;

    private UserDto sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = UserDto.builder()
                .id(1L)
                .email("admin@skilvorae.com")
                .firstName("System")
                .lastName("Administrator")
                .role(Role.ADMIN)
                .isEnabled(true)
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/admin/users")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return paginated list of system users")
        void getAllUsers_Success() throws Exception {
            PageImpl<UserDto> page = new PageImpl<>(List.of(sampleUser));
            when(userService.getAllUsers(any(PageRequest.class))).thenReturn(page);

            mockMvc.perform(get("/api/v1/admin/users")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].email").value("admin@skilvorae.com"))
                    .andExpect(jsonPath("$.content[0].role").value("ADMIN"));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/admin/users/{id}/status")
    class ToggleUserStatusTests {

        @Test
        @DisplayName("Should toggle user account enabled/disabled status")
        void toggleUserStatus_Success() throws Exception {
            doNothing().when(userService).toggleUserAccountStatus(1L, false);

            mockMvc.perform(put("/api/v1/admin/users/1/status")
                            .param("enabled", "false"))
                    .andExpect(status().isOk());

            verify(userService, times(1)).toggleUserAccountStatus(1L, false);
        }
    }
}
