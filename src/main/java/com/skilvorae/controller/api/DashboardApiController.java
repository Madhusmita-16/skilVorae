package com.skilvorae.controller.api;

import com.skilvorae.dto.ApiResponse;
import com.skilvorae.dto.DashboardStatsDto;
import com.skilvorae.entity.User;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        DashboardStatsDto stats = dashboardService.getDashboardStats(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Dashboard metrics loaded", stats));
    }
}
