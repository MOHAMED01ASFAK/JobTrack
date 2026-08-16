package com.jobtrack.controller;

import com.jobtrack.dto.request.InterviewRequest;
import com.jobtrack.dto.response.ApiResponse;
import com.jobtrack.dto.response.InterviewResponse;
import com.jobtrack.security.UserPrincipal;
import com.jobtrack.service.InterviewService;
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
 * REST Controller exposing endpoints for Interview tracking and management.
 */
@Tag(
        name = "Interviews",
        description = "Interview scheduling, round tracking, notes and meeting management"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
public class InterviewController {

    private static final Logger log = LoggerFactory.getLogger(InterviewController.class);

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    /**
     * Retrieve all interviews for a specific job application.
     *
     * GET /api/v1/jobs/{jobId}/interviews
     */
    @Operation(
            summary = "Get interviews for a job application",
            description = "Retrieves all scheduled interview rounds for a specific job application belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Interviews retrieved successfully"
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
    @GetMapping("/api/v1/jobs/{jobId}/interviews")
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getInterviewsByJobId(
            @PathVariable Long jobId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to get interviews for job ID: {} by user ID: {}", jobId, userId);

        List<InterviewResponse> responses = interviewService.getInterviewsByJobId(jobId, userId);
        return ResponseEntity.ok(ApiResponse.success("Interviews retrieved successfully", responses));
    }

    /**
     * Schedule a new interview for a job application.
     *
     * POST /api/v1/jobs/{jobId}/interviews
     */
    @Operation(
            summary = "Schedule an interview",
            description = "Schedules a new interview round for a specific job application belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Interview scheduled successfully"
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
    @PostMapping("/api/v1/jobs/{jobId}/interviews")
    public ResponseEntity<ApiResponse<InterviewResponse>> scheduleInterview(
            @PathVariable Long jobId,
            @Valid @RequestBody InterviewRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to schedule interview '{}' for job ID: {} by user ID: {}",
                request.getRoundName(), jobId, userId);

        InterviewResponse response = interviewService.scheduleInterview(jobId, request, userId);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Interview scheduled successfully", response));
    }

    /**
     * Update an existing interview.
     *
     * PUT /api/v1/interviews/{id}
     */
    @Operation(
            summary = "Update an interview",
            description = "Updates details, round status, or feedback notes of an existing interview belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Interview updated successfully"
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
                    description = "Interview not found for this user"
            )
    })
    @PutMapping("/api/v1/interviews/{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to update interview ID: {} by user ID: {}", id, userId);

        InterviewResponse response = interviewService.updateInterview(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Interview updated successfully", response));
    }

    /**
     * Delete an interview.
     *
     * DELETE /api/v1/interviews/{id}
     */
    @Operation(
            summary = "Delete an interview",
            description = "Deletes an interview round belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Interview deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Interview not found for this user"
            )
    })
    @DeleteMapping("/api/v1/interviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInterview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        log.info("REST request to delete interview ID: {} by user ID: {}", id, userId);

        interviewService.deleteInterview(id, userId);
        return ResponseEntity.ok(ApiResponse.ofMessage("Interview deleted successfully"));
    }
}
