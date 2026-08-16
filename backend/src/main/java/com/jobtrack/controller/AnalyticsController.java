package com.jobtrack.controller;

import com.jobtrack.dto.response.ApiResponse;
import com.jobtrack.dto.response.AnalyticsSummaryResponse;
import com.jobtrack.security.UserPrincipal;
import com.jobtrack.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller exposing career analytics and pipeline insights under /api/v1/analytics.
 */
@Tag(name = "Analytics & Insights", description = "Career pipeline metrics, conversion rates, and salary intelligence")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsController.class);

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Retrieve aggregated pipeline metrics and insights for the authenticated user.
     * GET /api/v1/analytics/summary
     */
    @Operation(
            summary = "Get career pipeline analytics summary",
            description = "Computes total applications, active pipeline count, interview conversion rate, offer rate, salary metrics, and stage breakdown strictly scoped to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Analytics summary retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid JWT token")
    })
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<AnalyticsSummaryResponse>> getAnalyticsSummary(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to retrieve career analytics summary for user ID: {}", userId);
        AnalyticsSummaryResponse summary = analyticsService.getAnalyticsSummary(userId);
        return ResponseEntity.ok(ApiResponse.success("Analytics summary retrieved successfully", summary));
    }
}
