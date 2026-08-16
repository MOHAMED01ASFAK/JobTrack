package com.jobtrack.dto.response;

import com.jobtrack.entity.enums.InterviewRoundType;
import com.jobtrack.entity.enums.InterviewStatus;

import java.time.LocalDateTime;

/**
 * Response payload representing an Interview.
 */
public class InterviewResponse {

    private Long id;
    private Long jobApplicationId;
    private String companyName;
    private String jobTitle;
    private String roundName;
    private InterviewRoundType roundType;
    private LocalDateTime scheduledTime;
    private String interviewerInfo;
    private String meetingLink;
    private InterviewStatus status;
    private String questionsAsked;
    private String feedbackNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InterviewResponse() {
    }

    public InterviewResponse(Long id, Long jobApplicationId, String companyName, String jobTitle,
                             String roundName, InterviewRoundType roundType, LocalDateTime scheduledTime,
                             String interviewerInfo, String meetingLink, InterviewStatus status,
                             String questionsAsked, String feedbackNotes,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jobApplicationId = jobApplicationId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.roundName = roundName;
        this.roundType = roundType;
        this.scheduledTime = scheduledTime;
        this.interviewerInfo = interviewerInfo;
        this.meetingLink = meetingLink;
        this.status = status;
        this.questionsAsked = questionsAsked;
        this.feedbackNotes = feedbackNotes;
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

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public InterviewRoundType getRoundType() {
        return roundType;
    }

    public void setRoundType(InterviewRoundType roundType) {
        this.roundType = roundType;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getInterviewerInfo() {
        return interviewerInfo;
    }

    public void setInterviewerInfo(String interviewerInfo) {
        this.interviewerInfo = interviewerInfo;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public String getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(String questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public String getFeedbackNotes() {
        return feedbackNotes;
    }

    public void setFeedbackNotes(String feedbackNotes) {
        this.feedbackNotes = feedbackNotes;
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
        private String roundName;
        private InterviewRoundType roundType;
        private LocalDateTime scheduledTime;
        private String interviewerInfo;
        private String meetingLink;
        private InterviewStatus status;
        private String questionsAsked;
        private String feedbackNotes;
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

        public Builder roundName(String roundName) {
            this.roundName = roundName;
            return this;
        }

        public Builder roundType(InterviewRoundType roundType) {
            this.roundType = roundType;
            return this;
        }

        public Builder scheduledTime(LocalDateTime scheduledTime) {
            this.scheduledTime = scheduledTime;
            return this;
        }

        public Builder interviewerInfo(String interviewerInfo) {
            this.interviewerInfo = interviewerInfo;
            return this;
        }

        public Builder meetingLink(String meetingLink) {
            this.meetingLink = meetingLink;
            return this;
        }

        public Builder status(InterviewStatus status) {
            this.status = status;
            return this;
        }

        public Builder questionsAsked(String questionsAsked) {
            this.questionsAsked = questionsAsked;
            return this;
        }

        public Builder feedbackNotes(String feedbackNotes) {
            this.feedbackNotes = feedbackNotes;
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

        public InterviewResponse build() {
            return new InterviewResponse(id, jobApplicationId, companyName, jobTitle,
                    roundName, roundType, scheduledTime, interviewerInfo, meetingLink,
                    status, questionsAsked, feedbackNotes, createdAt, updatedAt);
        }
    }
}
