package com.jobtrack.mapper;

import com.jobtrack.dto.request.InterviewRequest;
import com.jobtrack.dto.response.InterviewResponse;
import com.jobtrack.entity.Interview;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.enums.InterviewStatus;
import org.springframework.stereotype.Component;

/**
 * Component responsible for transforming Interview Entity to/from DTOs.
 */
@Component
public class InterviewMapper {

    public Interview toEntity(InterviewRequest request, JobApplication jobApplication) {
        if (request == null) {
            return null;
        }

        return Interview.builder()
                .jobApplication(jobApplication)
                .roundName(request.getRoundName())
                .roundType(request.getRoundType())
                .scheduledTime(request.getScheduledTime())
                .interviewerInfo(request.getInterviewerInfo())
                .meetingLink(request.getMeetingLink())
                .status(request.getStatus() != null ? request.getStatus() : InterviewStatus.SCHEDULED)
                .questionsAsked(request.getQuestionsAsked())
                .feedbackNotes(request.getFeedbackNotes())
                .build();
    }

    public void updateEntityFromRequest(Interview entity, InterviewRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getRoundName() != null && !request.getRoundName().isBlank()) {
            entity.setRoundName(request.getRoundName());
        }

        if (request.getRoundType() != null) {
            entity.setRoundType(request.getRoundType());
        }

        if (request.getScheduledTime() != null) {
            entity.setScheduledTime(request.getScheduledTime());
        }

        entity.setInterviewerInfo(request.getInterviewerInfo());
        entity.setMeetingLink(request.getMeetingLink());

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        entity.setQuestionsAsked(request.getQuestionsAsked());
        entity.setFeedbackNotes(request.getFeedbackNotes());
    }

    public InterviewResponse toResponse(Interview entity) {
        if (entity == null) {
            return null;
        }

        Long jobAppId = entity.getJobApplication() != null ? entity.getJobApplication().getId() : null;
        String company = entity.getJobApplication() != null ? entity.getJobApplication().getCompanyName() : null;
        String title = entity.getJobApplication() != null ? entity.getJobApplication().getJobTitle() : null;

        return InterviewResponse.builder()
                .id(entity.getId())
                .jobApplicationId(jobAppId)
                .companyName(company)
                .jobTitle(title)
                .roundName(entity.getRoundName())
                .roundType(entity.getRoundType())
                .scheduledTime(entity.getScheduledTime())
                .interviewerInfo(entity.getInterviewerInfo())
                .meetingLink(entity.getMeetingLink())
                .status(entity.getStatus())
                .questionsAsked(entity.getQuestionsAsked())
                .feedbackNotes(entity.getFeedbackNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
