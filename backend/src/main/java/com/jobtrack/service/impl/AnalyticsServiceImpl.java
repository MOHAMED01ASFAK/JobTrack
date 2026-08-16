package com.jobtrack.service.impl;

import com.jobtrack.dto.response.AnalyticsSummaryResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.WorkplaceType;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Implementation of AnalyticsService providing real-time pipeline statistics and benchmarks with user isolation.
 */
@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final JobApplicationRepository repository;

    public AnalyticsServiceImpl(JobApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public AnalyticsSummaryResponse getAnalyticsSummary(Long userId) {
        log.info("Computing job application analytics summary for user ID: {}", userId);
        List<JobApplication> applications = (userId != null)
                ? repository.findAllByUserId(userId)
                : repository.findAll();

        long total = applications.size();

        // 1. Status Breakdown
        Map<String, Long> statusBreakdown = new LinkedHashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusBreakdown.put(status.name(), 0L);
        }
        for (JobApplication app : applications) {
            if (app.getApplicationStatus() != null) {
                String key = app.getApplicationStatus().name();
                statusBreakdown.put(key, statusBreakdown.getOrDefault(key, 0L) + 1L);
            }
        }

        long applied = statusBreakdown.getOrDefault(ApplicationStatus.APPLIED.name(), 0L);
        long screening = statusBreakdown.getOrDefault(ApplicationStatus.SCREENING.name(), 0L);
        long interviewing = statusBreakdown.getOrDefault(ApplicationStatus.INTERVIEWING.name(), 0L);
        long offer = statusBreakdown.getOrDefault(ApplicationStatus.OFFER.name(), 0L);

        long activeCount = applied + screening + interviewing;

        // 2. Conversion Rates
        double interviewRate = total > 0
                ? BigDecimal.valueOf((double) (screening + interviewing + offer) / total * 100)
                .setScale(1, RoundingMode.HALF_UP).doubleValue()
                : 0.0;

        double offerRate = total > 0
                ? BigDecimal.valueOf((double) offer / total * 100)
                .setScale(1, RoundingMode.HALF_UP).doubleValue()
                : 0.0;

        // 3. Salary Aggregates
        BigDecimal minSalary = null;
        BigDecimal maxSalary = null;
        List<Double> salaryPoints = new ArrayList<>();

        for (JobApplication app : applications) {
            if (app.getSalaryMin() != null) {
                if (minSalary == null || app.getSalaryMin().compareTo(minSalary) < 0) {
                    minSalary = app.getSalaryMin();
                }
            }
            if (app.getSalaryMax() != null) {
                if (maxSalary == null || app.getSalaryMax().compareTo(maxSalary) > 0) {
                    maxSalary = app.getSalaryMax();
                }
            }
            if (app.getSalaryMax() != null && app.getSalaryMin() != null) {
                salaryPoints.add((app.getSalaryMin().doubleValue() + app.getSalaryMax().doubleValue()) / 2.0);
            } else if (app.getSalaryMax() != null) {
                salaryPoints.add(app.getSalaryMax().doubleValue());
            } else if (app.getSalaryMin() != null) {
                salaryPoints.add(app.getSalaryMin().doubleValue());
            }
        }

        Double avgSalary = salaryPoints.isEmpty()
                ? null
                : BigDecimal.valueOf(salaryPoints.stream().mapToDouble(Double::doubleValue).average().orElse(0.0))
                .setScale(0, RoundingMode.HALF_UP).doubleValue();

        // 4. Workplace Breakdown
        Map<String, Long> workplaceBreakdown = new LinkedHashMap<>();
        for (WorkplaceType wp : WorkplaceType.values()) {
            workplaceBreakdown.put(wp.name(), 0L);
        }
        for (JobApplication app : applications) {
            if (app.getWorkplaceType() != null) {
                String key = app.getWorkplaceType().name();
                workplaceBreakdown.put(key, workplaceBreakdown.getOrDefault(key, 0L) + 1L);
            }
        }

        // 5. Employment Breakdown
        Map<String, Long> employmentBreakdown = new LinkedHashMap<>();
        for (EmploymentType emp : EmploymentType.values()) {
            employmentBreakdown.put(emp.name(), 0L);
        }
        for (JobApplication app : applications) {
            if (app.getEmploymentType() != null) {
                String key = app.getEmploymentType().name();
                employmentBreakdown.put(key, employmentBreakdown.getOrDefault(key, 0L) + 1L);
            }
        }

        // 6. Monthly Trends (from appliedDate or createdAt)
        Map<String, Long> monthlyTrends = new TreeMap<>();
        for (JobApplication app : applications) {
            String monthKey = null;
            if (app.getAppliedDate() != null) {
                monthKey = app.getAppliedDate().format(MONTH_FORMATTER);
            } else if (app.getCreatedAt() != null) {
                monthKey = app.getCreatedAt().format(MONTH_FORMATTER);
            }
            if (monthKey != null) {
                monthlyTrends.put(monthKey, monthlyTrends.getOrDefault(monthKey, 0L) + 1L);
            }
        }

        return AnalyticsSummaryResponse.builder()
                .totalApplications(total)
                .activeApplications(activeCount)
                .interviewRatePercentage(interviewRate)
                .offerRatePercentage(offerRate)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .avgSalary(avgSalary)
                .statusBreakdown(statusBreakdown)
                .workplaceBreakdown(workplaceBreakdown)
                .employmentBreakdown(employmentBreakdown)
                .monthlyTrends(monthlyTrends)
                .build();
    }

    @Override
    public AnalyticsSummaryResponse getAnalyticsSummary() {
        return getAnalyticsSummary(null);
    }
}
