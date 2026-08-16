package com.jobtrack.service;

import com.jobtrack.dto.request.InterviewRequest;
import com.jobtrack.dto.response.InterviewResponse;

import java.util.List;

/**
 * Service interface defining business operations for Interviews with multi-user data isolation.
 */
public interface InterviewService {

    /**
     * Schedule a new interview for a specific job application belonging to the authenticated user.
     *
     * @param jobId   the parent job application ID
     * @param request the validated interview payload
     * @param userId  the authenticated user ID
     * @return the scheduled interview response
     */
    InterviewResponse scheduleInterview(Long jobId, InterviewRequest request, Long userId);

    /**
     * Retrieve all scheduled interviews for a specific job application belonging to the authenticated user.
     *
     * @param jobId  the parent job application ID
     * @param userId the authenticated user ID
     * @return list of interview responses
     */
    List<InterviewResponse> getInterviewsByJobId(Long jobId, Long userId);

    /**
     * Retrieve a specific interview by its ID and authenticated user ID.
     *
     * @param id     the interview ID
     * @param userId the authenticated user ID
     * @return the interview response
     */
    InterviewResponse getInterviewById(Long id, Long userId);

    /**
     * Update an existing interview by its ID and authenticated user ID.
     *
     * @param id      the interview ID to update
     * @param request the updated interview payload
     * @param userId  the authenticated user ID
     * @return the updated interview response
     */
    InterviewResponse updateInterview(Long id, InterviewRequest request, Long userId);

    /**
     * Delete an interview by its ID and authenticated user ID.
     *
     * @param id     the interview ID to delete
     * @param userId the authenticated user ID
     */
    void deleteInterview(Long id, Long userId);

    // Overloads for non-user-scoped operations or testing
    InterviewResponse scheduleInterview(Long jobId, InterviewRequest request);
    List<InterviewResponse> getInterviewsByJobId(Long jobId);
    InterviewResponse getInterviewById(Long id);
    InterviewResponse updateInterview(Long id, InterviewRequest request);
    void deleteInterview(Long id);
}
