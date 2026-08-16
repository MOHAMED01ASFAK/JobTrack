package com.jobtrack.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * JPA Entity representing a Follow-Up reminder associated with a Job Application.
 */
@Entity
@Table(name = "follow_ups", indexes = {
        @Index(name = "idx_followup_job", columnList = "job_application_id"),
        @Index(name = "idx_followup_due_date", columnList = "due_date"),
        @Index(name = "idx_followup_completed", columnList = "is_completed")
})
public class FollowUp extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted = false;

    public FollowUp() {
    }

    public FollowUp(JobApplication jobApplication, LocalDate dueDate, String contactName,
                    String contactEmail, String notes, boolean isCompleted) {
        this.jobApplication = jobApplication;
        this.dueDate = dueDate;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }

    public static Builder builder() {
        return new Builder();
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public static class Builder {
        private JobApplication jobApplication;
        private LocalDate dueDate;
        private String contactName;
        private String contactEmail;
        private String notes;
        private boolean isCompleted = false;

        public Builder jobApplication(JobApplication jobApplication) {
            this.jobApplication = jobApplication;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder contactName(String contactName) {
            this.contactName = contactName;
            return this;
        }

        public Builder contactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder isCompleted(boolean isCompleted) {
            this.isCompleted = isCompleted;
            return this;
        }

        public FollowUp build() {
            return new FollowUp(jobApplication, dueDate, contactName, contactEmail, notes, isCompleted);
        }
    }
}
