package com.jobtrack.service;

import com.jobtrack.dto.response.AnalyticsSummaryResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.WorkplaceType;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.impl.AnalyticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    private JobApplication job1;
    private JobApplication job2;

    @BeforeEach
    void setUp() {
        job1 = JobApplication.builder()
                .companyName("Google")
                .jobTitle("Senior Engineer")
                .applicationStatus(ApplicationStatus.INTERVIEWING)
                .workplaceType(WorkplaceType.HYBRID)
                .employmentType(EmploymentType.FULL_TIME)
                .salaryMin(new BigDecimal("180000.00"))
                .salaryMax(new BigDecimal("220000.00"))
                .appliedDate(LocalDate.of(2026, 8, 1))
                .build();

        job2 = JobApplication.builder()
                .companyName("Microsoft")
                .jobTitle("Cloud Architect")
                .applicationStatus(ApplicationStatus.OFFER)
                .workplaceType(WorkplaceType.REMOTE)
                .employmentType(EmploymentType.FULL_TIME)
                .salaryMin(new BigDecimal("200000.00"))
                .salaryMax(new BigDecimal("250000.00"))
                .appliedDate(LocalDate.of(2026, 8, 5))
                .build();
    }

    @Test
    @DisplayName("Should compute analytics summary across all applications")
    void testGetAnalyticsSummary_Success() {
        when(repository.findAll()).thenReturn(Arrays.asList(job1, job2));

        AnalyticsSummaryResponse summary = analyticsService.getAnalyticsSummary();

        assertNotNull(summary);
        assertEquals(2, summary.getTotalApplications());
        assertEquals(1, summary.getActiveApplications()); // job1 is INTERVIEWING
        assertEquals(100.0, summary.getInterviewRatePercentage()); // both are screening/interviewing/offer
        assertEquals(50.0, summary.getOfferRatePercentage()); // 1 offer out of 2
        assertEquals(new BigDecimal("180000.00"), summary.getMinSalary());
        assertEquals(new BigDecimal("250000.00"), summary.getMaxSalary());
        assertNotNull(summary.getAvgSalary());
        assertEquals(1L, summary.getStatusBreakdown().get("INTERVIEWING"));
        assertEquals(1L, summary.getStatusBreakdown().get("OFFER"));
        assertEquals(1L, summary.getWorkplaceBreakdown().get("HYBRID"));
        assertEquals(1L, summary.getWorkplaceBreakdown().get("REMOTE"));
    }

    @Test
    @DisplayName("Should compute analytics summary for specific user")
    void testGetAnalyticsSummary_WithUser() {
        when(repository.findAllByUserId(1L)).thenReturn(Collections.singletonList(job1));

        AnalyticsSummaryResponse summary = analyticsService.getAnalyticsSummary(1L);

        assertNotNull(summary);
        assertEquals(1, summary.getTotalApplications());
        assertEquals(1, summary.getActiveApplications());
        assertEquals(100.0, summary.getInterviewRatePercentage());
        assertEquals(0.0, summary.getOfferRatePercentage());
        assertEquals(1L, summary.getStatusBreakdown().get("INTERVIEWING"));
    }

    @Test
    @DisplayName("Should handle empty applications gracefully")
    void testGetAnalyticsSummary_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        AnalyticsSummaryResponse summary = analyticsService.getAnalyticsSummary();

        assertNotNull(summary);
        assertEquals(0, summary.getTotalApplications());
        assertEquals(0, summary.getActiveApplications());
        assertEquals(0.0, summary.getInterviewRatePercentage());
        assertEquals(0.0, summary.getOfferRatePercentage());
        assertNull(summary.getAvgSalary());
    }
}
