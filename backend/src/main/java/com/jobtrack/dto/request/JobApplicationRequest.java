package com.jobtrack.dto.request;

import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.WorkplaceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request payload for creating or updating a Job Application.
 */
public class JobApplicationRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    private String companyName;

    @NotBlank(message = "Job title is required")
    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;

    @Size(max = 255, message = "Job location must not exceed 255 characters")
    private String jobLocation;

    private WorkplaceType workplaceType;

    private EmploymentType employmentType;

    private ApplicationStatus applicationStatus;

    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum salary cannot be negative")
    private BigDecimal salaryMin;

    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum salary cannot be negative")
    private BigDecimal salaryMax;

    @Size(max = 10, message = "Currency code must not exceed 10 characters")
    private String salaryCurrency;

    @Size(max = 1000, message = "Job posting URL must not exceed 1000 characters")
    private String jobPostingUrl;

    private LocalDate appliedDate;

    private LocalDate deadlineDate;

    private String jobDescription;

    private String notes;

    @Min(value = 1, message = "Priority must be at least 1 (Low)")
    @Max(value = 5, message = "Priority must not exceed 5 (Very High)")
    private Integer priority;

    public JobApplicationRequest() {
    }

    public JobApplicationRequest(String companyName, String jobTitle, String jobLocation,
                                 WorkplaceType workplaceType, EmploymentType employmentType,
                                 ApplicationStatus applicationStatus, BigDecimal salaryMin,
                                 BigDecimal salaryMax, String salaryCurrency, String jobPostingUrl,
                                 LocalDate appliedDate, LocalDate deadlineDate, String jobDescription,
                                 String notes, Integer priority) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.jobLocation = jobLocation;
        this.workplaceType = workplaceType;
        this.employmentType = employmentType;
        this.applicationStatus = applicationStatus;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.salaryCurrency = salaryCurrency;
        this.jobPostingUrl = jobPostingUrl;
        this.appliedDate = appliedDate;
        this.deadlineDate = deadlineDate;
        this.jobDescription = jobDescription;
        this.notes = notes;
        this.priority = priority;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public WorkplaceType getWorkplaceType() {
        return workplaceType;
    }

    public void setWorkplaceType(WorkplaceType workplaceType) {
        this.workplaceType = workplaceType;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public String getJobPostingUrl() {
        return jobPostingUrl;
    }

    public void setJobPostingUrl(String jobPostingUrl) {
        this.jobPostingUrl = jobPostingUrl;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public static class Builder {
        private String companyName;
        private String jobTitle;
        private String jobLocation;
        private WorkplaceType workplaceType;
        private EmploymentType employmentType;
        private ApplicationStatus applicationStatus;
        private BigDecimal salaryMin;
        private BigDecimal salaryMax;
        private String salaryCurrency;
        private String jobPostingUrl;
        private LocalDate appliedDate;
        private LocalDate deadlineDate;
        private String jobDescription;
        private String notes;
        private Integer priority;

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public Builder jobLocation(String jobLocation) {
            this.jobLocation = jobLocation;
            return this;
        }

        public Builder workplaceType(WorkplaceType workplaceType) {
            this.workplaceType = workplaceType;
            return this;
        }

        public Builder employmentType(EmploymentType employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public Builder applicationStatus(ApplicationStatus applicationStatus) {
            this.applicationStatus = applicationStatus;
            return this;
        }

        public Builder salaryMin(BigDecimal salaryMin) {
            this.salaryMin = salaryMin;
            return this;
        }

        public Builder salaryMax(BigDecimal salaryMax) {
            this.salaryMax = salaryMax;
            return this;
        }

        public Builder salaryCurrency(String salaryCurrency) {
            this.salaryCurrency = salaryCurrency;
            return this;
        }

        public Builder jobPostingUrl(String jobPostingUrl) {
            this.jobPostingUrl = jobPostingUrl;
            return this;
        }

        public Builder appliedDate(LocalDate appliedDate) {
            this.appliedDate = appliedDate;
            return this;
        }

        public Builder deadlineDate(LocalDate deadlineDate) {
            this.deadlineDate = deadlineDate;
            return this;
        }

        public Builder jobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public JobApplicationRequest build() {
            return new JobApplicationRequest(companyName, jobTitle, jobLocation, workplaceType,
                    employmentType, applicationStatus, salaryMin, salaryMax, salaryCurrency,
                    jobPostingUrl, appliedDate, deadlineDate, jobDescription, notes, priority);
        }
    }
}
