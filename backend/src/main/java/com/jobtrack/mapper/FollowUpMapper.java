package com.jobtrack.mapper;

import com.jobtrack.dto.request.FollowUpRequest;
import com.jobtrack.dto.response.FollowUpResponse;
import com.jobtrack.entity.FollowUp;
import com.jobtrack.entity.JobApplication;
import org.springframework.stereotype.Component;

/**
 * Component responsible for transforming FollowUp Entity to/from DTOs.
 */
@Component
public class FollowUpMapper {

    public FollowUp toEntity(FollowUpRequest request, JobApplication jobApplication) {
        if (request == null) {
            return null;
        }

        return FollowUp.builder()
                .jobApplication(jobApplication)
                .dueDate(request.getDueDate())
                .contactName(request.getContactName())
                .contactEmail(request.getContactEmail())
                .notes(request.getNotes())
                .isCompleted(request.getIsCompleted() != null ? request.getIsCompleted() : false)
                .build();
    }

    public void updateEntityFromRequest(FollowUp entity, FollowUpRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getDueDate() != null) {
            entity.setDueDate(request.getDueDate());
        }

        entity.setContactName(request.getContactName());
        entity.setContactEmail(request.getContactEmail());
        entity.setNotes(request.getNotes());

        if (request.getIsCompleted() != null) {
            entity.setCompleted(request.getIsCompleted());
        }
    }

    public FollowUpResponse toResponse(FollowUp entity) {
        if (entity == null) {
            return null;
        }

        Long jobAppId = entity.getJobApplication() != null ? entity.getJobApplication().getId() : null;
        String company = entity.getJobApplication() != null ? entity.getJobApplication().getCompanyName() : null;
        String title = entity.getJobApplication() != null ? entity.getJobApplication().getJobTitle() : null;

        return FollowUpResponse.builder()
                .id(entity.getId())
                .jobApplicationId(jobAppId)
                .companyName(company)
                .jobTitle(title)
                .dueDate(entity.getDueDate())
                .contactName(entity.getContactName())
                .contactEmail(entity.getContactEmail())
                .notes(entity.getNotes())
                .isCompleted(entity.isCompleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
