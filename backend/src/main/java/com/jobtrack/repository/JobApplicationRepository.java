package com.jobtrack.repository;

import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for JobApplication entity operations.
 */
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByApplicationStatus(ApplicationStatus applicationStatus);

    long countByApplicationStatus(ApplicationStatus applicationStatus);

    List<JobApplication> findByCompanyNameContainingIgnoreCase(String companyName);

    List<JobApplication> findAllByOrderByCreatedAtDesc();

    // User-scoped queries for Multi-Tenant Data Isolation
    List<JobApplication> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    List<JobApplication> findAllByUserId(Long userId);

    Optional<JobApplication> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    List<JobApplication> findByUserIdAndApplicationStatus(Long userId, ApplicationStatus applicationStatus);

    long countByUserIdAndApplicationStatus(Long userId, ApplicationStatus applicationStatus);
}
