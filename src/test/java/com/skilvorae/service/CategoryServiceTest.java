package com.skilvorae.service;

import com.skilvorae.dto.CategoryDto;
import com.skilvorae.entity.Category;
import com.skilvorae.repository.CategoryRepository;
import com.skilvorae.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 test suite for CategoryServiceImpl in SkilVorae backend.
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Software Engineering");
        category.setSlug("software-engineering");
        category.setDescription("Courses on software development, clean code, and design patterns.");
    }

    @Test
    @DisplayName("Should return all active categories")
    void getAllCategories_Success() {
        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(List.of(category));

        List<CategoryDto> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Software Engineering", categories.get(0).getName());
    }

    @Test
    @DisplayName("Should find category by slug")
    void getCategoryBySlug_Success() {
        when(categoryRepository.findBySlug("software-engineering")).thenReturn(Optional.of(category));

        CategoryDto dto = categoryService.getCategoryBySlug("software-engineering");

        assertNotNull(dto);
        assertEquals("software-engineering", dto.getSlug());
    }
}
