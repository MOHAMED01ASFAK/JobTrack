package com.jobtrack.controller;

import com.jobtrack.dto.request.FollowUpRequest;
import com.jobtrack.dto.response.ApiResponse;
import com.jobtrack.dto.response.FollowUpResponse;
import com.jobtrack.security.UserPrincipal;
import com.jobtrack.service.FollowUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing endpoints for Follow-Up reminders and task management.
 */
@Tag(
        name = "Follow-Ups",
        description = "Follow-up reminders, due date tracking, and task completion toggles"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
public class FollowUpController {

    private static final Logger log = LoggerFactory.getLogger(FollowUpController.class);

    private final FollowUpService followUpService;

    public FollowUpController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }

    /**
     * Retrieve all follow-up reminders for the authenticated user, optionally filtered by completed status.
     *
     * GET /api/v1/follow-ups?completed=false
     */
    @Operation(
            summary = "Get all follow-ups",
            description = "Retrieves all follow-up reminders belonging to the authenticated user, with optional filtering by completion status (completed=true/false)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Follow-ups retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token"
            )
    })
    @GetMapping("/api/v1/follow-ups")
    public ResponseEntity<ApiResponse<List<FollowUpResponse>>> getAllFollowUps(
            @RequestParam(required = false) Boolean completed,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to get all follow-ups for user ID: {}, completed filter: {}", userId, completed);

        List<FollowUpResponse> responses = followUpService.getAllFollowUps(userId, completed);
        return ResponseEntity.ok(ApiResponse.success("Follow-ups retrieved successfully", responses));
    }

    /**
     * Create a new follow-up reminder for a job application.
     *
     * POST /api/v1/jobs/{jobId}/follow-ups
     */
    @Operation(
            summary = "Create a follow-up reminder",
            description = "Creates a new follow-up reminder under a specific job application belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Follow-up created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request validation payload"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Job application not found for this user"
            )
    })
    @PostMapping("/api/v1/jobs/{jobId}/follow-ups")
    public ResponseEntity<ApiResponse<FollowUpResponse>> createFollowUp(
            @PathVariable Long jobId,
            @Valid @RequestBody FollowUpRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to create follow-up for job ID: {} by user ID: {}", jobId, userId);

        FollowUpResponse response = followUpService.createFollowUp(jobId, request, userId);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Follow-up created successfully", response));
    }

    /**
     * Toggle the completion status of a follow-up reminder.
     *
     * PATCH /api/v1/follow-ups/{id}/toggle
     */
    @Operation(
            summary = "Toggle follow-up completion status",
            description = "Toggles the isCompleted boolean status of an existing follow-up reminder belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Follow-up completion status toggled successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Follow-up not found for this user"
            )
    })
    @PatchMapping("/api/v1/follow-ups/{id}/toggle")
    public ResponseEntity<ApiResponse<FollowUpResponse>> toggleFollowUp(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to toggle follow-up ID: {} by user ID: {}", id, userId);

        FollowUpResponse response = followUpService.toggleFollowUpCompletion(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Follow-up status toggled successfully", response));
    }
}
