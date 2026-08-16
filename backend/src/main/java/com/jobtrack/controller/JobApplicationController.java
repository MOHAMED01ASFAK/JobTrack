package com.jobtrack.controller;

import com.jobtrack.dto.request.JobApplicationRequest;
import com.jobtrack.dto.response.ApiResponse;
import com.jobtrack.dto.response.JobApplicationResponse;
import com.jobtrack.security.UserPrincipal;
import com.jobtrack.service.JobApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * REST Controller exposing CRUD endpoints for Job Applications
 * under /api/v1/jobs.
 */
@Tag(
        name = "Job Applications",
        description = "Full CRUD operations, search, filtering, and status progression for job applications"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/jobs")
public class JobApplicationController {

    private static final Logger log =
            LoggerFactory.getLogger(JobApplicationController.class);

    private final JobApplicationService jobApplicationService;


    public JobApplicationController(
            JobApplicationService jobApplicationService) {

        this.jobApplicationService = jobApplicationService;
    }


    /**
     * Create a new job application.
     *
     * POST /api/v1/jobs
     */
    @Operation(
            summary = "Create a new job application",
            description = "Creates a new job application entity associated with the currently authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Job application created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request validation payload"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<JobApplicationResponse>> createJob(
            @Valid @RequestBody JobApplicationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId =
                userPrincipal != null
                        ? userPrincipal.getId()
                        : null;

        log.info(
                "REST request to create job application: {} for user ID: {}",
                request.getCompanyName(),
                userId
        );

        JobApplicationResponse response =
                jobApplicationService.createJobApplication(
                        request,
                        userId
                );

        return ResponseEntity
                .status(201)
                .body(
                        ApiResponse.success(
                                "Job application created successfully",
                                response
                        )
                );
    }


    /**
     * Retrieve all job applications.
     *
     * GET /api/v1/jobs
     */
    @Operation(
            summary = "Get all job applications",
            description = "Retrieves all job applications created by the authenticated user in descending order of creation."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Job applications retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getAllJobs(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId =
                userPrincipal != null
                        ? userPrincipal.getId()
                        : null;

        log.info(
                "REST request to retrieve all job applications for user ID: {}",
                userId
        );

        List<JobApplicationResponse> responses =
                jobApplicationService.getAllJobApplications(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job applications retrieved successfully",
                        responses
                )
        );
    }


    /**
     * Retrieve a specific job application.
     *
     * GET /api/v1/jobs/{id}
     */
    @Operation(
            summary = "Get job application by ID",
            description = "Retrieves details of a specific job application belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Job application retrieved successfully"
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> getJobById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId =
                userPrincipal != null
                        ? userPrincipal.getId()
                        : null;

        log.info(
                "REST request to get job application by ID: {} for user ID: {}",
                id,
                userId
        );

        JobApplicationResponse response =
                jobApplicationService.getJobApplicationById(
                        id,
                        userId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job application retrieved successfully",
                        response
                )
        );
    }


    /**
     * Update an existing job application.
     *
     * PUT /api/v1/jobs/{id}
     */
    @Operation(
            summary = "Update a job application",
            description = "Updates an existing job application belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Job application updated successfully"
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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId =
                userPrincipal != null
                        ? userPrincipal.getId()
                        : null;

        log.info(
                "REST request to update job application ID: {} for user ID: {}",
                id,
                userId
        );

        JobApplicationResponse response =
                jobApplicationService.updateJobApplication(
                        id,
                        request,
                        userId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job application updated successfully",
                        response
                )
        );
    }


    /**
     * Delete a job application.
     *
     * DELETE /api/v1/jobs/{id}
     */
    @Operation(
            summary = "Delete a job application",
            description = "Deletes an existing job application belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Job application deleted successfully"
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId =
                userPrincipal != null
                        ? userPrincipal.getId()
                        : null;

        log.info(
                "REST request to delete job application ID: {} for user ID: {}",
                id,
                userId
        );

        jobApplicationService.deleteJobApplication(
                id,
                userId
        );

        return ResponseEntity.ok(
                ApiResponse.ofMessage(
                        "Job application deleted successfully"
                )
        );
    }


    /**
     * Export all job applications as CSV.
     *
     * GET /api/v1/jobs/export/csv
     *
     * This endpoint returns a real Resource instead of directly returning
     * byte[], which avoids the content-negotiation problem that was causing
     * the "No acceptable representation" 500 error.
     */
    @Operation(
            summary = "Export job applications as CSV",
            description = "Exports all job applications belonging to the authenticated user into an RFC-4180 compliant CSV file."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "CSV export generated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token"
            )
    })
    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportJobsCsv(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId =
                userPrincipal != null
                        ? userPrincipal.getId()
                        : null;

        log.info(
                "REST request to export job applications as CSV for user ID: {}",
                userId
        );

        List<JobApplicationResponse> jobs =
                jobApplicationService.getAllJobApplications(userId);


        /*
         * UTF-8 BOM.
         *
         * This helps Microsoft Excel correctly recognize UTF-8 CSV files,
         * especially when the CSV contains Indian/local-language characters.
         */
        StringBuilder csv = new StringBuilder();

        csv.append('\uFEFF');


        /*
         * CSV Header
         */
        csv.append(
                "ID,"
                        + "Company Name,"
                        + "Job Title,"
                        + "Location,"
                        + "Workplace Model,"
                        + "Employment Type,"
                        + "Status,"
                        + "Priority,"
                        + "Salary Min,"
                        + "Salary Max,"
                        + "Currency,"
                        + "Applied Date,"
                        + "Deadline Date,"
                        + "Job Posting URL,"
                        + "Notes,"
                        + "Created At,"
                        + "Updated At"
                        + "\r\n"
        );


        /*
         * CSV Rows
         */
        for (JobApplicationResponse job : jobs) {

            csv.append(escapeCsv(job.getId())).append(",");
            csv.append(escapeCsv(job.getCompanyName())).append(",");
            csv.append(escapeCsv(job.getJobTitle())).append(",");
            csv.append(escapeCsv(job.getJobLocation())).append(",");
            csv.append(escapeCsv(job.getWorkplaceType())).append(",");
            csv.append(escapeCsv(job.getEmploymentType())).append(",");
            csv.append(escapeCsv(job.getApplicationStatus())).append(",");
            csv.append(escapeCsv(job.getPriority())).append(",");
            csv.append(escapeCsv(job.getSalaryMin())).append(",");
            csv.append(escapeCsv(job.getSalaryMax())).append(",");
            csv.append(escapeCsv(job.getSalaryCurrency())).append(",");
            csv.append(escapeCsv(job.getAppliedDate())).append(",");
            csv.append(escapeCsv(job.getDeadlineDate())).append(",");
            csv.append(escapeCsv(job.getJobPostingUrl())).append(",");
            csv.append(escapeCsv(job.getNotes())).append(",");
            csv.append(escapeCsv(job.getCreatedAt())).append(",");
            csv.append(escapeCsv(job.getUpdatedAt())).append("\r\n");
        }


        /*
         * Convert CSV text into UTF-8 bytes.
         */
        byte[] csvBytes =
                csv.toString().getBytes(StandardCharsets.UTF_8);


        /*
         * Wrap the byte array as a Spring Resource.
         *
         * This is the important fix for the previous
         * "No acceptable representation" error.
         */
        ByteArrayResource resource =
                new ByteArrayResource(csvBytes);


        /*
         * Build the download response.
         */
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.parseMediaType(
                        "text/csv;charset=UTF-8"
                )
        );

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(
                                "jobtrack_applications.csv",
                                StandardCharsets.UTF_8
                        )
                        .build()
        );

        headers.setContentLength(csvBytes.length);

        headers.setCacheControl(
                "no-cache, no-store, must-revalidate"
        );


        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }


    /**
     * Escape a value according to CSV rules.
     *
     * Values containing comma, quote, newline, or carriage return
     * are wrapped in double quotes.
     *
     * Existing double quotes are doubled.
     */
    private String escapeCsv(Object value) {

        if (value == null) {
            return "";
        }

        String str = value.toString();

        if (
                str.contains(",")
                        || str.contains("\"")
                        || str.contains("\n")
                        || str.contains("\r")
        ) {

            return "\""
                    + str.replace("\"", "\"\"")
                    + "\"";
        }

        return str;
    }
}