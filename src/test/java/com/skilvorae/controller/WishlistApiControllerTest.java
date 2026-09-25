package com.skilvorae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilvorae.dto.CourseDto;
import com.skilvorae.enums.CourseCategory;
import com.skilvorae.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration unit test suite for WishlistApiController in SkilVorae backend.
 */
@WebMvcTest(WishlistApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WishlistApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WishlistService wishlistService;

    private CourseDto wishlistCourse;

    @BeforeEach
    void setUp() {
        wishlistCourse = CourseDto.builder()
                .id(3L)
                .title("Data Science & Machine Learning with Python")
                .category(CourseCategory.DATA_SCIENCE)
                .price(BigDecimal.valueOf(39.99))
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/wishlist")
    class GetWishlistTests {

        @Test
        @DisplayName("Should return wishlisted courses for logged-in user")
        void getWishlist_Success() throws Exception {
            when(wishlistService.getUserWishlist(any())).thenReturn(List.of(wishlistCourse));

            mockMvc.perform(get("/api/v1/wishlist"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Data Science & Machine Learning with Python"));

            verify(wishlistService, times(1)).getUserWishlist(any());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/wishlist/course/{courseId}")
    class AddToWishlistTests {

        @Test
        @DisplayName("Should add course to user wishlist")
        void addToWishlist_Success() throws Exception {
            doNothing().when(wishlistService).addToWishlist(eq(3L), any());

            mockMvc.perform(post("/api/v1/wishlist/course/3"))
                    .andExpect(status().isOk());

            verify(wishlistService, times(1)).addToWishlist(eq(3L), any());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/wishlist/course/{courseId}")
    class RemoveFromWishlistTests {

        @Test
        @DisplayName("Should remove course from user wishlist")
        void removeFromWishlist_Success() throws Exception {
            doNothing().when(wishlistService).removeFromWishlist(eq(3L), any());

            mockMvc.perform(delete("/api/v1/wishlist/course/3"))
                    .andExpect(status().isNoContent());

            verify(wishlistService, times(1)).removeFromWishlist(eq(3L), any());
        }
    }
}
