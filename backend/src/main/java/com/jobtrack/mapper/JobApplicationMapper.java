package com.jobtrack.mapper;

import com.jobtrack.dto.request.JobApplicationRequest;
import com.jobtrack.dto.response.JobApplicationResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.enums.ApplicationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Component responsible for transforming JobApplication Entity to/from DTOs.
 */
@Component
public class JobApplicationMapper {

    public JobApplication toEntity(JobApplicationRequest request) {
        if (request == null) {
            return null;
        }

        return JobApplication.builder()
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .jobLocation(request.getJobLocation())
                .workplaceType(request.getWorkplaceType())
                .employmentType(request.getEmploymentType())
                .applicationStatus(request.getApplicationStatus() != null ? request.getApplicationStatus() : ApplicationStatus.APPLIED)
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .salaryCurrency(request.getSalaryCurrency() != null && !request.getSalaryCurrency().isBlank() ? request.getSalaryCurrency() : "INR")
                .jobPostingUrl(request.getJobPostingUrl())
                .appliedDate(request.getAppliedDate() != null ? request.getAppliedDate() : LocalDate.now())
                .deadlineDate(request.getDeadlineDate())
                .jobDescription(request.getJobDescription())
                .notes(request.getNotes())
                .priority(request.getPriority() != null ? request.getPriority() : 3)
                .build();
    }

    public void updateEntityFromRequest(JobApplication entity, JobApplicationRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setCompanyName(request.getCompanyName());
        entity.setJobTitle(request.getJobTitle());
        entity.setJobLocation(request.getJobLocation());
        entity.setWorkplaceType(request.getWorkplaceType());
        entity.setEmploymentType(request.getEmploymentType());
        
        if (request.getApplicationStatus() != null) {
            entity.setApplicationStatus(request.getApplicationStatus());
        }
        
        entity.setSalaryMin(request.getSalaryMin());
        entity.setSalaryMax(request.getSalaryMax());
        
        if (request.getSalaryCurrency() != null && !request.getSalaryCurrency().isBlank()) {
            entity.setSalaryCurrency(request.getSalaryCurrency());
        }
        
        entity.setJobPostingUrl(request.getJobPostingUrl());
        
        if (request.getAppliedDate() != null) {
            entity.setAppliedDate(request.getAppliedDate());
        }
        
        entity.setDeadlineDate(request.getDeadlineDate());
        entity.setJobDescription(request.getJobDescription());
        entity.setNotes(request.getNotes());
        
        if (request.getPriority() != null) {
            entity.setPriority(request.getPriority());
        }
    }

    public JobApplicationResponse toResponse(JobApplication entity) {
        if (entity == null) {
            return null;
        }

        return JobApplicationResponse.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .jobTitle(entity.getJobTitle())
                .jobLocation(entity.getJobLocation())
                .workplaceType(entity.getWorkplaceType())
                .employmentType(entity.getEmploymentType())
                .applicationStatus(entity.getApplicationStatus())
                .salaryMin(entity.getSalaryMin())
                .salaryMax(entity.getSalaryMax())
                .salaryCurrency(entity.getSalaryCurrency())
                .jobPostingUrl(entity.getJobPostingUrl())
                .appliedDate(entity.getAppliedDate())
                .deadlineDate(entity.getDeadlineDate())
                .jobDescription(entity.getJobDescription())
                .notes(entity.getNotes())
                .priority(entity.getPriority())
                .priorityLabel(JobApplicationResponse.getPriorityLabel(entity.getPriority()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
