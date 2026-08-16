package com.jobtrack.repository;

import com.jobtrack.entity.Interview;
import com.jobtrack.entity.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Interview entity operations with user isolation.
 */
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByJobApplicationIdOrderByScheduledTimeAsc(Long jobApplicationId);

    // User-scoped queries for Multi-Tenant Data Isolation
    List<Interview> findByJobApplicationIdAndJobApplicationUserIdOrderByScheduledTimeAsc(Long jobApplicationId, Long userId);

    Optional<Interview> findByIdAndJobApplicationUserId(Long id, Long userId);

    boolean existsByIdAndJobApplicationUserId(Long id, Long userId);

    void deleteByIdAndJobApplicationUserId(Long id, Long userId);

    long countByJobApplicationUserId(Long userId);

    long countByJobApplicationUserIdAndStatus(Long userId, InterviewStatus status);
}
