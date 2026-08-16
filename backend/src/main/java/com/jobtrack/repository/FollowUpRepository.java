package com.jobtrack.repository;

import com.jobtrack.entity.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for FollowUp entity operations with user isolation.
 */
@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    List<FollowUp> findByJobApplicationIdOrderByDueDateAsc(Long jobApplicationId);

    // User-scoped queries for Multi-Tenant Data Isolation
    List<FollowUp> findAllByJobApplicationUserIdOrderByDueDateAsc(Long userId);

    List<FollowUp> findAllByJobApplicationUserIdAndIsCompletedOrderByDueDateAsc(Long userId, boolean isCompleted);

    List<FollowUp> findByJobApplicationIdAndJobApplicationUserIdOrderByDueDateAsc(Long jobApplicationId, Long userId);

    Optional<FollowUp> findByIdAndJobApplicationUserId(Long id, Long userId);

    boolean existsByIdAndJobApplicationUserId(Long id, Long userId);

    void deleteByIdAndJobApplicationUserId(Long id, Long userId);

    long countByJobApplicationUserIdAndIsCompleted(Long userId, boolean isCompleted);
}
