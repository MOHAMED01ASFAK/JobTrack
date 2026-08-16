package com.jobtrack.dto.response;

import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.WorkplaceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response payload representing a Job Application.
 */
public class JobApplicationResponse {

    private Long id;
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
    private String priorityLabel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobApplicationResponse() {
    }

    public JobApplicationResponse(Long id, String companyName, String jobTitle, String jobLocation,
                                  WorkplaceType workplaceType, EmploymentType employmentType,
                                  ApplicationStatus applicationStatus, BigDecimal salaryMin,
                                  BigDecimal salaryMax, String salaryCurrency, String jobPostingUrl,
                                  LocalDate appliedDate, LocalDate deadlineDate, String jobDescription,
                                  String notes, Integer priority, String priorityLabel,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
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
        this.priorityLabel = priorityLabel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static String getPriorityLabel(Integer priority) {
        if (priority == null) return "Medium";
        return switch (priority) {
            case 1 -> "Low";
            case 2 -> "Medium-Low";
            case 3 -> "Medium";
            case 4 -> "High";
            case 5 -> "Very High";
            default -> "Medium";
        };
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPriorityLabel() {
        return priorityLabel;
    }

    public void setPriorityLabel(String priorityLabel) {
        this.priorityLabel = priorityLabel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Builder {
        private Long id;
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
        private String priorityLabel;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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
            this.priorityLabel = JobApplicationResponse.getPriorityLabel(priority);
            return this;
        }

        public Builder priorityLabel(String priorityLabel) {
            this.priorityLabel = priorityLabel;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public JobApplicationResponse build() {
            return new JobApplicationResponse(id, companyName, jobTitle, jobLocation, workplaceType,
                    employmentType, applicationStatus, salaryMin, salaryMax, salaryCurrency,
                    jobPostingUrl, appliedDate, deadlineDate, jobDescription, notes, priority,
                    priorityLabel != null ? priorityLabel : JobApplicationResponse.getPriorityLabel(priority),
                    createdAt, updatedAt);
        }
    }
}
