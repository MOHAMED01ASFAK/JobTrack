package com.jobtrack.service.impl;

import com.jobtrack.dto.request.InterviewRequest;
import com.jobtrack.dto.response.InterviewResponse;
import com.jobtrack.entity.Interview;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.mapper.InterviewMapper;
import com.jobtrack.repository.InterviewRepository;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.InterviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Production-ready implementation of InterviewService with user data isolation and transaction management.
 */
@Service
public class InterviewServiceImpl implements InterviewService {

    private static final Logger log = LoggerFactory.getLogger(InterviewServiceImpl.class);

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewMapper interviewMapper;

    public InterviewServiceImpl(InterviewRepository interviewRepository,
                                JobApplicationRepository jobApplicationRepository,
                                InterviewMapper interviewMapper) {
        this.interviewRepository = interviewRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.interviewMapper = interviewMapper;
    }

    @Override
    @Transactional
    public InterviewResponse scheduleInterview(Long jobId, InterviewRequest request, Long userId) {
        log.info("Scheduling interview '{}' for job ID: {}, userId: {}", request.getRoundName(), jobId, userId);

        JobApplication jobApplication = (userId != null)
                ? jobApplicationRepository.findByIdAndUserId(jobId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", jobId))
                : jobApplicationRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", jobId));

        Interview interview = interviewMapper.toEntity(request, jobApplication);
        Interview savedInterview = interviewRepository.save(interview);

        log.info("Interview scheduled successfully with ID: {} for job ID: {}", savedInterview.getId(), jobId);
        return interviewMapper.toResponse(savedInterview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsByJobId(Long jobId, Long userId) {
        log.debug("Fetching all interviews for job ID: {}, userId: {}", jobId, userId);

        if (userId != null) {
            if (!jobApplicationRepository.existsByIdAndUserId(jobId, userId)) {
                throw new ResourceNotFoundException("JobApplication", "id", jobId);
            }
            return interviewRepository.findByJobApplicationIdAndJobApplicationUserIdOrderByScheduledTimeAsc(jobId, userId)
                    .stream()
                    .map(interviewMapper::toResponse)
                    .toList();
        } else {
            if (!jobApplicationRepository.existsById(jobId)) {
                throw new ResourceNotFoundException("JobApplication", "id", jobId);
            }
            return interviewRepository.findByJobApplicationIdOrderByScheduledTimeAsc(jobId)
                    .stream()
                    .map(interviewMapper::toResponse)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(Long id, Long userId) {
        log.debug("Fetching interview with ID: {}, userId: {}", id, userId);

        Interview interview = (userId != null)
                ? interviewRepository.findByIdAndJobApplicationUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id))
                : interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id));

        return interviewMapper.toResponse(interview);
    }

    @Override
    @Transactional
    public InterviewResponse updateInterview(Long id, InterviewRequest request, Long userId) {
        log.info("Updating interview with ID: {}, userId: {}", id, userId);

        Interview existingInterview = (userId != null)
                ? interviewRepository.findByIdAndJobApplicationUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id))
                : interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id));

        interviewMapper.updateEntityFromRequest(existingInterview, request);
        Interview updatedInterview = interviewRepository.save(existingInterview);

        log.info("Interview with ID: {} updated successfully", updatedInterview.getId());
        return interviewMapper.toResponse(updatedInterview);
    }

    @Override
    @Transactional
    public void deleteInterview(Long id, Long userId) {
        log.info("Deleting interview with ID: {}, userId: {}", id, userId);

        if (userId != null) {
            if (!interviewRepository.existsByIdAndJobApplicationUserId(id, userId)) {
                throw new ResourceNotFoundException("Interview", "id", id);
            }
            interviewRepository.deleteByIdAndJobApplicationUserId(id, userId);
        } else {
            if (!interviewRepository.existsById(id)) {
                throw new ResourceNotFoundException("Interview", "id", id);
            }
            interviewRepository.deleteById(id);
        }

        log.info("Interview with ID: {} deleted successfully", id);
    }

    // Overloads for non-user-scoped operations
    @Override
    @Transactional
    public InterviewResponse scheduleInterview(Long jobId, InterviewRequest request) {
        return scheduleInterview(jobId, request, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsByJobId(Long jobId) {
        return getInterviewsByJobId(jobId, null);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(Long id) {
        return getInterviewById(id, null);
    }

    @Override
    @Transactional
    public InterviewResponse updateInterview(Long id, InterviewRequest request) {
        return updateInterview(id, request, null);
    }

    @Override
    @Transactional
    public void deleteInterview(Long id) {
        deleteInterview(id, null);
    }
}
