package com.jobtrack.service;

import com.jobtrack.dto.request.FollowUpRequest;
import com.jobtrack.dto.response.FollowUpResponse;

import java.util.List;

/**
 * Service interface defining business operations for Follow-Up reminders with multi-user data isolation.
 */
public interface FollowUpService {

    /**
     * Create a new follow-up reminder under a specific job application belonging to the authenticated user.
     *
     * @param jobId   the parent job application ID
     * @param request the validated follow-up payload
     * @param userId  the authenticated user ID
     * @return the created follow-up response
     */
    FollowUpResponse createFollowUp(Long jobId, FollowUpRequest request, Long userId);

    /**
     * Retrieve all follow-ups for the authenticated user, optionally filtered by completion status.
     *
     * @param userId    the authenticated user ID
     * @param completed optional boolean filter (true = completed only, false = pending only, null = all)
     * @return list of follow-up responses
     */
    List<FollowUpResponse> getAllFollowUps(Long userId, Boolean completed);

    /**
     * Retrieve a specific follow-up by its ID and authenticated user ID.
     *
     * @param id     the follow-up ID
     * @param userId the authenticated user ID
     * @return the follow-up response
     */
    FollowUpResponse getFollowUpById(Long id, Long userId);

    /**
     * Toggle the completion status (isCompleted) of a follow-up reminder.
     *
     * @param id     the follow-up ID to toggle
     * @param userId the authenticated user ID
     * @return the updated follow-up response
     */
    FollowUpResponse toggleFollowUpCompletion(Long id, Long userId);

    /**
     * Update an existing follow-up reminder.
     *
     * @param id      the follow-up ID to update
     * @param request the updated follow-up payload
     * @param userId  the authenticated user ID
     * @return the updated follow-up response
     */
    FollowUpResponse updateFollowUp(Long id, FollowUpRequest request, Long userId);

    /**
     * Delete a follow-up reminder.
     *
     * @param id     the follow-up ID to delete
     * @param userId the authenticated user ID
     */
    void deleteFollowUp(Long id, Long userId);

    // Overloads for non-user-scoped operations
    FollowUpResponse createFollowUp(Long jobId, FollowUpRequest request);
    List<FollowUpResponse> getAllFollowUps(Boolean completed);
    FollowUpResponse getFollowUpById(Long id);
    FollowUpResponse toggleFollowUpCompletion(Long id);
    FollowUpResponse updateFollowUp(Long id, FollowUpRequest request);
    void deleteFollowUp(Long id);
}
