package com.jobtrack.repository;

import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.User;
import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.Role;
import com.jobtrack.entity.enums.WorkplaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JobApplicationRepositoryTest {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should persist and retrieve JobApplication by ID")
    void testSaveAndFindById() {
        JobApplication job = JobApplication.builder()
                .companyName("Flipkart")
                .jobTitle("Software Development Engineer")
                .jobLocation("Bangalore, India")
                .workplaceType(WorkplaceType.HYBRID)
                .employmentType(EmploymentType.FULL_TIME)
                .applicationStatus(ApplicationStatus.APPLIED)
                .salaryMin(new BigDecimal("2200000.00"))
                .salaryMax(new BigDecimal("3000000.00"))
                .salaryCurrency("INR")
                .appliedDate(LocalDate.now())
                .priority(4)
                .build();

        JobApplication saved = jobApplicationRepository.save(job);

        assertNotNull(saved.getId());
        Optional<JobApplication> found = jobApplicationRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Flipkart", found.get().getCompanyName());
        assertEquals("INR", found.get().getSalaryCurrency());
        assertEquals(4, found.get().getPriority());
    }

    @Test
    @DisplayName("Should find JobApplications by Status")
    void testFindByApplicationStatus() {
        JobApplication job1 = JobApplication.builder()
                .companyName("Swiggy")
                .jobTitle("Backend Engineer")
                .applicationStatus(ApplicationStatus.APPLIED)
                .build();

        JobApplication job2 = JobApplication.builder()
                .companyName("Zomato")
                .jobTitle("Senior Backend Engineer")
                .applicationStatus(ApplicationStatus.INTERVIEWING)
                .build();

        jobApplicationRepository.save(job1);
        jobApplicationRepository.save(job2);

        List<JobApplication> appliedList = jobApplicationRepository.findByApplicationStatus(ApplicationStatus.APPLIED);
        assertEquals(1, appliedList.size());
        assertEquals("Swiggy", appliedList.get(0).getCompanyName());
    }

    @Test
    @DisplayName("Should persist User and query JobApplications scoped to User ID")
    void testUserScopedQueries() {
        User user1 = userRepository.save(User.builder()
                .username("user1")
                .email("user1@test.com")
                .password("hash1")
                .fullName("User One")
                .role(Role.ROLE_USER)
                .build());

        User user2 = userRepository.save(User.builder()
                .username("user2")
                .email("user2@test.com")
                .password("hash2")
                .fullName("User Two")
                .role(Role.ROLE_USER)
                .build());

        JobApplication jobUser1 = jobApplicationRepository.save(JobApplication.builder()
                .user(user1)
                .companyName("Uber")
                .jobTitle("Backend Lead")
                .applicationStatus(ApplicationStatus.APPLIED)
                .build());

        JobApplication jobUser2 = jobApplicationRepository.save(JobApplication.builder()
                .user(user2)
                .companyName("Lyft")
                .jobTitle("Staff Engineer")
                .applicationStatus(ApplicationStatus.INTERVIEWING)
                .build());

        List<JobApplication> user1Jobs = jobApplicationRepository.findAllByUserId(user1.getId());
        assertEquals(1, user1Jobs.size());
        assertEquals("Uber", user1Jobs.get(0).getCompanyName());

        Optional<JobApplication> found = jobApplicationRepository.findByIdAndUserId(jobUser1.getId(), user1.getId());
        assertTrue(found.isPresent());

        // User2 cannot access User1's job
        Optional<JobApplication> forbidden = jobApplicationRepository.findByIdAndUserId(jobUser1.getId(), user2.getId());
        assertTrue(forbidden.isEmpty());
    }
}
