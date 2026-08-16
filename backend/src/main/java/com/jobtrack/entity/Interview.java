package com.jobtrack.entity;

import com.jobtrack.entity.enums.InterviewRoundType;
import com.jobtrack.entity.enums.InterviewStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA Entity representing an Interview scheduled for a Job Application.
 */
@Entity
@Table(name = "interviews", indexes = {
        @Index(name = "idx_interview_job", columnList = "job_application_id"),
        @Index(name = "idx_interview_scheduled_time", columnList = "scheduled_time"),
        @Index(name = "idx_interview_status", columnList = "status")
})
public class Interview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    @Column(name = "round_name", nullable = false)
    private String roundName;

    @Enumerated(EnumType.STRING)
    @Column(name = "round_type", nullable = false, length = 50)
    private InterviewRoundType roundType;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(name = "interviewer_info")
    private String interviewerInfo;

    @Column(name = "meeting_link", length = 1000)
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(name = "questions_asked", columnDefinition = "TEXT")
    private String questionsAsked;

    @Column(name = "feedback_notes", columnDefinition = "TEXT")
    private String feedbackNotes;

    public Interview() {
    }

    public Interview(JobApplication jobApplication, String roundName, InterviewRoundType roundType,
                     LocalDateTime scheduledTime, String interviewerInfo, String meetingLink,
                     InterviewStatus status, String questionsAsked, String feedbackNotes) {
        this.jobApplication = jobApplication;
        this.roundName = roundName;
        this.roundType = roundType;
        this.scheduledTime = scheduledTime;
        this.interviewerInfo = interviewerInfo;
        this.meetingLink = meetingLink;
        this.status = status != null ? status : InterviewStatus.SCHEDULED;
        this.questionsAsked = questionsAsked;
        this.feedbackNotes = feedbackNotes;
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
        private JobApplication jobApplication;
        private String roundName;
        private InterviewRoundType roundType;
        private LocalDateTime scheduledTime;
        private String interviewerInfo;
        private String meetingLink;
        private InterviewStatus status = InterviewStatus.SCHEDULED;
        private String questionsAsked;
        private String feedbackNotes;

        public Builder jobApplication(JobApplication jobApplication) {
            this.jobApplication = jobApplication;
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

        public Interview build() {
            return new Interview(jobApplication, roundName, roundType, scheduledTime,
                    interviewerInfo, meetingLink, status, questionsAsked, feedbackNotes);
        }
    }
}
