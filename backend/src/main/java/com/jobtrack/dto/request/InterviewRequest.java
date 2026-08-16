package com.jobtrack.dto.request;

import com.jobtrack.entity.enums.InterviewRoundType;
import com.jobtrack.entity.enums.InterviewStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Request payload for creating or updating an Interview.
 */
public class InterviewRequest {

    @NotBlank(message = "Round name is required")
    @Size(max = 255, message = "Round name must not exceed 255 characters")
    private String roundName;

    @NotNull(message = "Round type is required")
    private InterviewRoundType roundType;

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledTime;

    @Size(max = 255, message = "Interviewer info must not exceed 255 characters")
    private String interviewerInfo;

    @Size(max = 1000, message = "Meeting link must not exceed 1000 characters")
    private String meetingLink;

    private InterviewStatus status;

    private String questionsAsked;

    private String feedbackNotes;

    public InterviewRequest() {
    }

    public InterviewRequest(String roundName, InterviewRoundType roundType, LocalDateTime scheduledTime,
                            String interviewerInfo, String meetingLink, InterviewStatus status,
                            String questionsAsked, String feedbackNotes) {
        this.roundName = roundName;
        this.roundType = roundType;
        this.scheduledTime = scheduledTime;
        this.interviewerInfo = interviewerInfo;
        this.meetingLink = meetingLink;
        this.status = status;
        this.questionsAsked = questionsAsked;
        this.feedbackNotes = feedbackNotes;
    }

    public static Builder builder() {
        return new Builder();
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

    public static class Builder {
        private String roundName;
        private InterviewRoundType roundType;
        private LocalDateTime scheduledTime;
        private String interviewerInfo;
        private String meetingLink;
        private InterviewStatus status;
        private String questionsAsked;
        private String feedbackNotes;

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

        public InterviewRequest build() {
            return new InterviewRequest(roundName, roundType, scheduledTime, interviewerInfo,
                    meetingLink, status, questionsAsked, feedbackNotes);
        }
    }
}
