package com.jobtrack.dto.response;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Response DTO encapsulating aggregated analytics and career pipeline metrics.
 */
public class AnalyticsSummaryResponse {

    private long totalApplications;
    private long activeApplications;
    private double interviewRatePercentage;
    private double offerRatePercentage;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private Double avgSalary;
    private Map<String, Long> statusBreakdown;
    private Map<String, Long> workplaceBreakdown;
    private Map<String, Long> employmentBreakdown;
    private Map<String, Long> monthlyTrends;

    public AnalyticsSummaryResponse() {
    }

    public AnalyticsSummaryResponse(long totalApplications, long activeApplications,
                                    double interviewRatePercentage, double offerRatePercentage,
                                    BigDecimal minSalary, BigDecimal maxSalary, Double avgSalary,
                                    Map<String, Long> statusBreakdown, Map<String, Long> workplaceBreakdown,
                                    Map<String, Long> employmentBreakdown, Map<String, Long> monthlyTrends) {
        this.totalApplications = totalApplications;
        this.activeApplications = activeApplications;
        this.interviewRatePercentage = interviewRatePercentage;
        this.offerRatePercentage = offerRatePercentage;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.avgSalary = avgSalary;
        this.statusBreakdown = statusBreakdown;
        this.workplaceBreakdown = workplaceBreakdown;
        this.employmentBreakdown = employmentBreakdown;
        this.monthlyTrends = monthlyTrends;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getActiveApplications() {
        return activeApplications;
    }

    public void setActiveApplications(long activeApplications) {
        this.activeApplications = activeApplications;
    }

    public double getInterviewRatePercentage() {
        return interviewRatePercentage;
    }

    public void setInterviewRatePercentage(double interviewRatePercentage) {
        this.interviewRatePercentage = interviewRatePercentage;
    }

    public double getOfferRatePercentage() {
        return offerRatePercentage;
    }

    public void setOfferRatePercentage(double offerRatePercentage) {
        this.offerRatePercentage = offerRatePercentage;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Double getAvgSalary() {
        return avgSalary;
    }

    public void setAvgSalary(Double avgSalary) {
        this.avgSalary = avgSalary;
    }

    public Map<String, Long> getStatusBreakdown() {
        return statusBreakdown;
    }

    public void setStatusBreakdown(Map<String, Long> statusBreakdown) {
        this.statusBreakdown = statusBreakdown;
    }

    public Map<String, Long> getWorkplaceBreakdown() {
        return workplaceBreakdown;
    }

    public void setWorkplaceBreakdown(Map<String, Long> workplaceBreakdown) {
        this.workplaceBreakdown = workplaceBreakdown;
    }

    public Map<String, Long> getEmploymentBreakdown() {
        return employmentBreakdown;
    }

    public void setEmploymentBreakdown(Map<String, Long> employmentBreakdown) {
        this.employmentBreakdown = employmentBreakdown;
    }

    public Map<String, Long> getMonthlyTrends() {
        return monthlyTrends;
    }

    public void setMonthlyTrends(Map<String, Long> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }

    public static class Builder {
        private long totalApplications;
        private long activeApplications;
        private double interviewRatePercentage;
        private double offerRatePercentage;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private Double avgSalary;
        private Map<String, Long> statusBreakdown;
        private Map<String, Long> workplaceBreakdown;
        private Map<String, Long> employmentBreakdown;
        private Map<String, Long> monthlyTrends;

        public Builder totalApplications(long totalApplications) {
            this.totalApplications = totalApplications;
            return this;
        }

        public Builder activeApplications(long activeApplications) {
            this.activeApplications = activeApplications;
            return this;
        }

        public Builder interviewRatePercentage(double interviewRatePercentage) {
            this.interviewRatePercentage = interviewRatePercentage;
            return this;
        }

        public Builder offerRatePercentage(double offerRatePercentage) {
            this.offerRatePercentage = offerRatePercentage;
            return this;
        }

        public Builder minSalary(BigDecimal minSalary) {
            this.minSalary = minSalary;
            return this;
        }

        public Builder maxSalary(BigDecimal maxSalary) {
            this.maxSalary = maxSalary;
            return this;
        }

        public Builder avgSalary(Double avgSalary) {
            this.avgSalary = avgSalary;
            return this;
        }

        public Builder statusBreakdown(Map<String, Long> statusBreakdown) {
            this.statusBreakdown = statusBreakdown;
            return this;
        }

        public Builder workplaceBreakdown(Map<String, Long> workplaceBreakdown) {
            this.workplaceBreakdown = workplaceBreakdown;
            return this;
        }

        public Builder employmentBreakdown(Map<String, Long> employmentBreakdown) {
            this.employmentBreakdown = employmentBreakdown;
            return this;
        }

        public Builder monthlyTrends(Map<String, Long> monthlyTrends) {
            this.monthlyTrends = monthlyTrends;
            return this;
        }

        public AnalyticsSummaryResponse build() {
            return new AnalyticsSummaryResponse(totalApplications, activeApplications,
                    interviewRatePercentage, offerRatePercentage, minSalary, maxSalary,
                    avgSalary, statusBreakdown, workplaceBreakdown, employmentBreakdown, monthlyTrends);
        }
    }
}
