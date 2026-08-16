package com.jobtrack.service.impl;

import com.jobtrack.dto.request.FollowUpRequest;
import com.jobtrack.dto.response.FollowUpResponse;
import com.jobtrack.entity.FollowUp;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.mapper.FollowUpMapper;
import com.jobtrack.repository.FollowUpRepository;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.FollowUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Production-ready implementation of FollowUpService with user data isolation and transaction management.
 */
@Service
public class FollowUpServiceImpl implements FollowUpService {

    private static final Logger log = LoggerFactory.getLogger(FollowUpServiceImpl.class);

    private final FollowUpRepository followUpRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final FollowUpMapper followUpMapper;

    public FollowUpServiceImpl(FollowUpRepository followUpRepository,
                               JobApplicationRepository jobApplicationRepository,
                               FollowUpMapper followUpMapper) {
        this.followUpRepository = followUpRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.followUpMapper = followUpMapper;
    }

    @Override
    @Transactional
    public FollowUpResponse createFollowUp(Long jobId, FollowUpRequest request, Long userId) {
        log.info("Creating follow-up reminder for job ID: {}, userId: {}", jobId, userId);

        JobApplication jobApplication = (userId != null)
                ? jobApplicationRepository.findByIdAndUserId(jobId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", jobId))
                : jobApplicationRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", jobId));

        FollowUp followUp = followUpMapper.toEntity(request, jobApplication);
        FollowUp savedFollowUp = followUpRepository.save(followUp);

        log.info("Follow-up reminder created successfully with ID: {} for job ID: {}", savedFollowUp.getId(), jobId);
        return followUpMapper.toResponse(savedFollowUp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUpResponse> getAllFollowUps(Long userId, Boolean completed) {
        log.debug("Fetching all follow-ups for userId: {}, completed filter: {}", userId, completed);

        List<FollowUp> followUps;
        if (userId != null) {
            if (completed != null) {
                followUps = followUpRepository.findAllByJobApplicationUserIdAndIsCompletedOrderByDueDateAsc(userId, completed);
            } else {
                followUps = followUpRepository.findAllByJobApplicationUserIdOrderByDueDateAsc(userId);
            }
        } else {
            if (completed != null) {
                followUps = followUpRepository.findAll().stream()
                        .filter(f -> f.isCompleted() == completed)
                        .toList();
            } else {
                followUps = followUpRepository.findAll();
            }
        }

        return followUps.stream()
                .map(followUpMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FollowUpResponse getFollowUpById(Long id, Long userId) {
        log.debug("Fetching follow-up with ID: {}, userId: {}", id, userId);

        FollowUp followUp = (userId != null)
                ? followUpRepository.findByIdAndJobApplicationUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("FollowUp", "id", id))
                : followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FollowUp", "id", id));

        return followUpMapper.toResponse(followUp);
    }

    @Override
    @Transactional
    public FollowUpResponse toggleFollowUpCompletion(Long id, Long userId) {
        log.info("Toggling follow-up completion status for ID: {}, userId: {}", id, userId);

        FollowUp existingFollowUp = (userId != null)
                ? followUpRepository.findByIdAndJobApplicationUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("FollowUp", "id", id))
                : followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FollowUp", "id", id));

        existingFollowUp.setCompleted(!existingFollowUp.isCompleted());
        FollowUp updatedFollowUp = followUpRepository.save(existingFollowUp);

        log.info("Follow-up with ID: {} completion toggled to: {}", id, updatedFollowUp.isCompleted());
        return followUpMapper.toResponse(updatedFollowUp);
    }

    @Override
    @Transactional
    public FollowUpResponse updateFollowUp(Long id, FollowUpRequest request, Long userId) {
        log.info("Updating follow-up with ID: {}, userId: {}", id, userId);

        FollowUp existingFollowUp = (userId != null)
                ? followUpRepository.findByIdAndJobApplicationUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("FollowUp", "id", id))
                : followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FollowUp", "id", id));

        followUpMapper.updateEntityFromRequest(existingFollowUp, request);
        FollowUp updatedFollowUp = followUpRepository.save(existingFollowUp);

        log.info("Follow-up with ID: {} updated successfully", id);
        return followUpMapper.toResponse(updatedFollowUp);
    }

    @Override
    @Transactional
    public void deleteFollowUp(Long id, Long userId) {
        log.info("Deleting follow-up with ID: {}, userId: {}", id, userId);

        if (userId != null) {
            if (!followUpRepository.existsByIdAndJobApplicationUserId(id, userId)) {
                throw new ResourceNotFoundException("FollowUp", "id", id);
            }
            followUpRepository.deleteByIdAndJobApplicationUserId(id, userId);
        } else {
            if (!followUpRepository.existsById(id)) {
                throw new ResourceNotFoundException("FollowUp", "id", id);
            }
            followUpRepository.deleteById(id);
        }

        log.info("Follow-up with ID: {} deleted successfully", id);
    }

    // Overloads for non-user-scoped operations
    @Override
    @Transactional
    public FollowUpResponse createFollowUp(Long jobId, FollowUpRequest request) {
        return createFollowUp(jobId, request, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUpResponse> getAllFollowUps(Boolean completed) {
        return getAllFollowUps(null, completed);
    }

    @Override
    @Transactional(readOnly = true)
    public FollowUpResponse getFollowUpById(Long id) {
        return getFollowUpById(id, null);
    }

    @Override
    @Transactional
    public FollowUpResponse toggleFollowUpCompletion(Long id) {
        return toggleFollowUpCompletion(id, null);
    }

    @Override
    @Transactional
    public FollowUpResponse updateFollowUp(Long id, FollowUpRequest request) {
        return updateFollowUp(id, request, null);
    }

    @Override
    @Transactional
    public void deleteFollowUp(Long id) {
        deleteFollowUp(id, null);
    }
}
