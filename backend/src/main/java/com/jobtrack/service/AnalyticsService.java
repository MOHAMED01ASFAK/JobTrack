package com.jobtrack.service;

import com.jobtrack.dto.response.AnalyticsSummaryResponse;

/**
 * Service interface for computing aggregated career and application metrics with user isolation.
 */
public interface AnalyticsService {

    /**
     * Compute comprehensive summary analytics for a specific user.
     *
     * @param userId the authenticated user ID
     * @return AnalyticsSummaryResponse containing aggregated stats
     */
    AnalyticsSummaryResponse getAnalyticsSummary(Long userId);

    /**
     * Compute comprehensive summary analytics across all job applications.
     *
     * @return AnalyticsSummaryResponse containing aggregated stats
     */
    AnalyticsSummaryResponse getAnalyticsSummary();
}
