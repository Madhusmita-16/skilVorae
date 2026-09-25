package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CategoryDto;
import com.skilvorae.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for CategoryApiController in SkilVorae backend.
 */
@WebMvcTest(CategoryApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private CategoryDto sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = CategoryDto.builder()
                .id(1L)
                .name("Software Engineering")
                .slug("software-engineering")
                .description("Courses on software development, clean code, and design patterns.")
                .courseCount(15)
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/categories")
    class GetAllCategoriesTests {

        @Test
        @DisplayName("Should return list of all active course categories")
        void getAllCategories_Success() throws Exception {
            when(categoryService.getAllCategories()).thenReturn(List.of(sampleCategory));

            mockMvc.perform(get("/api/v1/categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Software Engineering"))
                    .andExpect(jsonPath("$[0].slug").value("software-engineering"))
                    .andExpect(jsonPath("$[0].courseCount").value(15));

            verify(categoryService, times(1)).getAllCategories();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/categories/slug/{slug}")
    class GetCategoryBySlugTests {

        @Test
        @DisplayName("Should return category details by valid slug")
        void getCategoryBySlug_Success() throws Exception {
            when(categoryService.getCategoryBySlug("software-engineering")).thenReturn(sampleCategory);

            mockMvc.perform(get("/api/v1/categories/slug/software-engineering"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Software Engineering"));

            verify(categoryService, times(1)).getCategoryBySlug("software-engineering");
        }
    }
}
