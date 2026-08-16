package com.jobtrack.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request payload for creating or updating a Follow-Up reminder.
 */
public class FollowUpRequest {

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @Size(max = 255, message = "Contact name must not exceed 255 characters")
    private String contactName;

    @Email(message = "Contact email must be a valid email address")
    @Size(max = 255, message = "Contact email must not exceed 255 characters")
    private String contactEmail;

    private String notes;

    @JsonProperty("isCompleted")
    private Boolean isCompleted;

    public FollowUpRequest() {
    }

    public FollowUpRequest(LocalDate dueDate, String contactName, String contactEmail, String notes, Boolean isCompleted) {
        this.dueDate = dueDate;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }

    public static Builder builder() {
        return new Builder();
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

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public static class Builder {
        private LocalDate dueDate;
        private String contactName;
        private String contactEmail;
        private String notes;
        private Boolean isCompleted;

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

        public Builder isCompleted(Boolean isCompleted) {
            this.isCompleted = isCompleted;
            return this;
        }

        public FollowUpRequest build() {
            return new FollowUpRequest(dueDate, contactName, contactEmail, notes, isCompleted);
        }
    }
}
