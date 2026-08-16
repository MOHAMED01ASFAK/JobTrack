package com.jobtrack.entity;

import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.WorkplaceType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity representing a Job Application in the database.
 */
@Entity
@Table(name = "job_applications", indexes = {
        @Index(name = "idx_job_company", columnList = "company_name"),
        @Index(name = "idx_job_status", columnList = "application_status"),
        @Index(name = "idx_job_applied_date", columnList = "applied_date"),
        @Index(name = "idx_job_user", columnList = "user_id")
})
public class JobApplication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_location")
    private String jobLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "workplace_type", length = 30)
    private WorkplaceType workplaceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", length = 30)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false, length = 30)
    private ApplicationStatus applicationStatus = ApplicationStatus.APPLIED;

    @Column(name = "salary_min", precision = 12, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 12, scale = 2)
    private BigDecimal salaryMax;

    @Column(name = "salary_currency", length = 10)
    private String salaryCurrency = "INR";

    @Column(name = "job_posting_url", length = 1000)
    private String jobPostingUrl;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @Column(name = "deadline_date")
    private LocalDate deadlineDate;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Priority level from 1 (Low) to 5 (Very High).
     * 1: Low, 2: Medium-Low, 3: Medium, 4: High, 5: Very High.
     */
    @Column(name = "priority")
    private Integer priority = 3;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Interview> interviews = new ArrayList<>();

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FollowUp> followUps = new ArrayList<>();

    public JobApplication() {
    }

    public JobApplication(User user, String companyName, String jobTitle, String jobLocation,
                          WorkplaceType workplaceType, EmploymentType employmentType,
                          ApplicationStatus applicationStatus, BigDecimal salaryMin,
                          BigDecimal salaryMax, String salaryCurrency, String jobPostingUrl,
                          LocalDate appliedDate, LocalDate deadlineDate, String jobDescription,
                          String notes, Integer priority) {
        this.user = user;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.jobLocation = jobLocation;
        this.workplaceType = workplaceType;
        this.employmentType = employmentType;
        this.applicationStatus = applicationStatus != null ? applicationStatus : ApplicationStatus.APPLIED;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.salaryCurrency = salaryCurrency != null ? salaryCurrency : "INR";
        this.jobPostingUrl = jobPostingUrl;
        this.appliedDate = appliedDate;
        this.deadlineDate = deadlineDate;
        this.jobDescription = jobDescription;
        this.notes = notes;
        this.priority = priority != null ? priority : 3;
    }

    public JobApplication(String companyName, String jobTitle, String jobLocation,
                          WorkplaceType workplaceType, EmploymentType employmentType,
                          ApplicationStatus applicationStatus, BigDecimal salaryMin,
                          BigDecimal salaryMax, String salaryCurrency, String jobPostingUrl,
                          LocalDate appliedDate, LocalDate deadlineDate, String jobDescription,
                          String notes, Integer priority) {
        this(null, companyName, jobTitle, jobLocation, workplaceType, employmentType,
                applicationStatus, salaryMin, salaryMax, salaryCurrency, jobPostingUrl,
                appliedDate, deadlineDate, jobDescription, notes, priority);
    }

    public static Builder builder() {
        return new Builder();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Interview> getInterviews() {
        return interviews;
    }

    public void setInterviews(List<Interview> interviews) {
        this.interviews = interviews;
    }

    public List<FollowUp> getFollowUps() {
        return followUps;
    }

    public void setFollowUps(List<FollowUp> followUps) {
        this.followUps = followUps;
    }

    public static class Builder {
        private User user;
        private String companyName;
        private String jobTitle;
        private String jobLocation;
        private WorkplaceType workplaceType;
        private EmploymentType employmentType;
        private ApplicationStatus applicationStatus = ApplicationStatus.APPLIED;
        private BigDecimal salaryMin;
        private BigDecimal salaryMax;
        private String salaryCurrency = "INR";
        private String jobPostingUrl;
        private LocalDate appliedDate;
        private LocalDate deadlineDate;
        private String jobDescription;
        private String notes;
        private Integer priority = 3;

        public Builder user(User user) {
            this.user = user;
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
            return this;
        }

        public JobApplication build() {
            return new JobApplication(user, companyName, jobTitle, jobLocation, workplaceType,
                    employmentType, applicationStatus, salaryMin, salaryMax, salaryCurrency,
                    jobPostingUrl, appliedDate, deadlineDate, jobDescription, notes, priority);
        }
    }
}
