package com.jobtrack.service.impl;

import com.jobtrack.dto.request.JobApplicationRequest;
import com.jobtrack.dto.response.JobApplicationResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.User;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.mapper.JobApplicationMapper;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.repository.UserRepository;
import com.jobtrack.service.JobApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Production-ready implementation of JobApplicationService with user isolation and transaction management.
 */
@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private static final Logger log = LoggerFactory.getLogger(JobApplicationServiceImpl.class);

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;
    private final JobApplicationMapper jobApplicationMapper;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository,
                                     UserRepository userRepository,
                                     JobApplicationMapper jobApplicationMapper) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    @Override
    @Transactional
    public JobApplicationResponse createJobApplication(JobApplicationRequest request, Long userId) {
        log.info("Creating new job application for company: '{}', role: '{}', userId: {}",
                request.getCompanyName(), request.getJobTitle(), userId);

        JobApplication entity = jobApplicationMapper.toEntity(request);

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
            entity.setUser(user);
        }

        JobApplication savedEntity = jobApplicationRepository.save(entity);
        log.info("Job application created successfully with ID: {} for user: {}", savedEntity.getId(), userId);
        return jobApplicationMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getAllJobApplications(Long userId) {
        log.debug("Fetching all job applications for user ID: {}", userId);
        List<JobApplication> applications = (userId != null)
                ? jobApplicationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                : jobApplicationRepository.findAllByOrderByCreatedAtDesc();

        return applications.stream()
                .map(jobApplicationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getJobApplicationById(Long id, Long userId) {
        log.debug("Fetching job application with ID: {} for user ID: {}", id, userId);
        JobApplication entity = (userId != null)
                ? jobApplicationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id))
                : jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id));

        return jobApplicationMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public JobApplicationResponse updateJobApplication(Long id, JobApplicationRequest request, Long userId) {
        log.info("Updating job application with ID: {} for user ID: {}", id, userId);

        JobApplication existingEntity = (userId != null)
                ? jobApplicationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id))
                : jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id));

        jobApplicationMapper.updateEntityFromRequest(existingEntity, request);
        JobApplication updatedEntity = jobApplicationRepository.save(existingEntity);

        log.info("Job application with ID: {} updated successfully for user ID: {}", updatedEntity.getId(), userId);
        return jobApplicationMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteJobApplication(Long id, Long userId) {
        log.info("Deleting job application with ID: {} for user ID: {}", id, userId);

        if (userId != null) {
            if (!jobApplicationRepository.existsByIdAndUserId(id, userId)) {
                throw new ResourceNotFoundException("JobApplication", "id", id);
            }
            jobApplicationRepository.deleteByIdAndUserId(id, userId);
        } else {
            if (!jobApplicationRepository.existsById(id)) {
                throw new ResourceNotFoundException("JobApplication", "id", id);
            }
            jobApplicationRepository.deleteById(id);
        }

        log.info("Job application with ID: {} deleted successfully for user ID: {}", id, userId);
    }

    // Backwards-compatible overloads
    @Override
    @Transactional
    public JobApplicationResponse createJobApplication(JobApplicationRequest request) {
        return createJobApplication(request, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getAllJobApplications() {
        return getAllJobApplications(null);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getJobApplicationById(Long id) {
        return getJobApplicationById(id, null);
    }

    @Override
    @Transactional
    public JobApplicationResponse updateJobApplication(Long id, JobApplicationRequest request) {
        return updateJobApplication(id, request, null);
    }

    @Override
    @Transactional
    public void deleteJobApplication(Long id) {
        deleteJobApplication(id, null);
    }
}
