package com.journal.backend.controller;

import com.journal.backend.dto.ApiResponse;
import com.journal.backend.dto.DashboardAnalyticsResponseDTO;
import com.journal.backend.services.JournalAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardAnalyticsController {

    private final JournalAnalyticsService dashboardAnalyticsService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardAnalyticsResponseDTO>> getDashboardAnalytics(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "7") int periodDays
    ) {
        try {
            DashboardAnalyticsResponseDTO response = dashboardAnalyticsService.getDashboardAnalytics(userDetails.getUsername(), periodDays);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard analytics fetched successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to fetch dashboard analytics", null));
        }
    }
}