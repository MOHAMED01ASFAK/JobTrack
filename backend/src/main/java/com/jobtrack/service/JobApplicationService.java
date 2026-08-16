package com.jobtrack.service;

import com.jobtrack.dto.request.JobApplicationRequest;
import com.jobtrack.dto.response.JobApplicationResponse;

import java.util.List;

/**
 * Service interface defining business operations for Job Applications with multi-user data isolation.
 */
public interface JobApplicationService {

    /**
     * Create and persist a new job application for the specified user.
     *
     * @param request the validated job application request payload
     * @param userId  the authenticated user ID
     * @return the created job application response
     */
    JobApplicationResponse createJobApplication(JobApplicationRequest request, Long userId);

    /**
     * Retrieve all job applications belonging to the specified user.
     *
     * @param userId the authenticated user ID
     * @return list of job application responses
     */
    List<JobApplicationResponse> getAllJobApplications(Long userId);

    /**
     * Retrieve a specific job application by its ID and user ID.
     *
     * @param id     the job application ID
     * @param userId the authenticated user ID
     * @return the job application response
     */
    JobApplicationResponse getJobApplicationById(Long id, Long userId);

    /**
     * Update an existing job application by its ID and user ID.
     *
     * @param id      the job application ID to update
     * @param request the updated job application payload
     * @param userId  the authenticated user ID
     * @return the updated job application response
     */
    JobApplicationResponse updateJobApplication(Long id, JobApplicationRequest request, Long userId);

    /**
     * Delete a job application by its ID and user ID.
     *
     * @param id     the job application ID to delete
     * @param userId the authenticated user ID
     */
    void deleteJobApplication(Long id, Long userId);

    // Default overloads for backwards compatibility
    JobApplicationResponse createJobApplication(JobApplicationRequest request);
    List<JobApplicationResponse> getAllJobApplications();
    JobApplicationResponse getJobApplicationById(Long id);
    JobApplicationResponse updateJobApplication(Long id, JobApplicationRequest request);
    void deleteJobApplication(Long id);
}
