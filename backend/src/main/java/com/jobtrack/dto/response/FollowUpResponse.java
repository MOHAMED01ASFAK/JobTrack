package com.jobtrack.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response payload representing a Follow-Up reminder.
 */
public class FollowUpResponse {

    private Long id;
    private Long jobApplicationId;
    private String companyName;
    private String jobTitle;
    private LocalDate dueDate;
    private String contactName;
    private String contactEmail;
    private String notes;

    @JsonProperty("isCompleted")
    private boolean isCompleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FollowUpResponse() {
    }

    public FollowUpResponse(Long id, Long jobApplicationId, String companyName, String jobTitle,
                            LocalDate dueDate, String contactName, String contactEmail,
                            String notes, boolean isCompleted,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jobApplicationId = jobApplicationId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.dueDate = dueDate;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.notes = notes;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobApplicationId() {
        return jobApplicationId;
    }

    public void setJobApplicationId(Long jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
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

    @JsonProperty("isCompleted")
    public boolean isCompleted() {
        return isCompleted;
    }

    @JsonProperty("isCompleted")
    public void setCompleted(boolean completed) {
        isCompleted = completed;
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
        private Long jobApplicationId;
        private String companyName;
        private String jobTitle;
        private LocalDate dueDate;
        private String contactName;
        private String contactEmail;
        private String notes;
        private boolean isCompleted;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder jobApplicationId(Long jobApplicationId) {
            this.jobApplicationId = jobApplicationId;
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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FollowUpResponse build() {
            return new FollowUpResponse(id, jobApplicationId, companyName, jobTitle,
                    dueDate, contactName, contactEmail, notes, isCompleted,
                    createdAt, updatedAt);
        }
    }
}
