package com.jobtrack.service;

import com.jobtrack.dto.request.JobApplicationRequest;
import com.jobtrack.dto.response.JobApplicationResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.User;
import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.WorkplaceType;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.mapper.JobApplicationMapper;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.repository.UserRepository;
import com.jobtrack.service.impl.JobApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private JobApplicationMapper jobApplicationMapper = new JobApplicationMapper();

    @InjectMocks
    private JobApplicationServiceImpl jobApplicationService;

    private User sampleUser;
    private JobApplication sampleEntity;
    private JobApplicationRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .build();
        sampleUser.setId(1L);

        sampleEntity = JobApplication.builder()
                .user(sampleUser)
                .companyName("Google")
                .jobTitle("Software Engineer")
                .jobLocation("Bangalore, India")
                .workplaceType(WorkplaceType.HYBRID)
                .employmentType(EmploymentType.FULL_TIME)
                .applicationStatus(ApplicationStatus.APPLIED)
                .salaryMin(new BigDecimal("2500000.00"))
                .salaryMax(new BigDecimal("3500000.00"))
                .salaryCurrency("INR")
                .appliedDate(LocalDate.now())
                .priority(4)
                .build();
        sampleEntity.setId(1L);

        sampleRequest = JobApplicationRequest.builder()
                .companyName("Google")
                .jobTitle("Software Engineer")
                .jobLocation("Bangalore, India")
                .workplaceType(WorkplaceType.HYBRID)
                .employmentType(EmploymentType.FULL_TIME)
                .applicationStatus(ApplicationStatus.APPLIED)
                .salaryMin(new BigDecimal("2500000.00"))
                .salaryMax(new BigDecimal("3500000.00"))
                .salaryCurrency("INR")
                .appliedDate(LocalDate.now())
                .priority(4)
                .build();
    }

    @Test
    @DisplayName("Should create job application for user successfully")
    void testCreateJobApplication_WithUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(sampleEntity);

        JobApplicationResponse response = jobApplicationService.createJobApplication(sampleRequest, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Google", response.getCompanyName());
        assertEquals("Software Engineer", response.getJobTitle());
        assertEquals("INR", response.getSalaryCurrency());
        assertEquals(4, response.getPriority());
        assertEquals("High", response.getPriorityLabel());

        verify(jobApplicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should retrieve all job applications for user")
    void testGetAllJobApplications_WithUser() {
        when(jobApplicationRepository.findAllByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(sampleEntity));

        List<JobApplicationResponse> responses = jobApplicationService.getAllJobApplications(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Google", responses.get(0).getCompanyName());
    }

    @Test
    @DisplayName("Should retrieve job application by ID and User ID")
    void testGetJobApplicationById_UserSuccess() {
        when(jobApplicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(sampleEntity));

        JobApplicationResponse response = jobApplicationService.getJobApplicationById(1L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Google", response.getCompanyName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User B tries to access User A's job")
    void testGetJobApplicationById_UserIsolationForbidden() {
        when(jobApplicationRepository.findByIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jobApplicationService.getJobApplicationById(1L, 2L));
    }

    @Test
    @DisplayName("Should update job application successfully for owner user")
    void testUpdateJobApplication_WithUser() {
        when(jobApplicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(sampleEntity));
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(sampleEntity);

        sampleRequest.setCompanyName("Alphabet");
        JobApplicationResponse response = jobApplicationService.updateJobApplication(1L, sampleRequest, 1L);

        assertNotNull(response);
        verify(jobApplicationRepository, times(1)).save(sampleEntity);
    }

    @Test
    @DisplayName("Should delete job application successfully for owner user")
    void testDeleteJobApplication_WithUser() {
        when(jobApplicationRepository.existsByIdAndUserId(1L, 1L)).thenReturn(true);
        doNothing().when(jobApplicationRepository).deleteByIdAndUserId(1L, 1L);

        assertDoesNotThrow(() -> jobApplicationService.deleteJobApplication(1L, 1L));
        verify(jobApplicationRepository, times(1)).deleteByIdAndUserId(1L, 1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User B tries to delete User A's job")
    void testDeleteJobApplication_UserIsolationForbidden() {
        when(jobApplicationRepository.existsByIdAndUserId(1L, 2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> jobApplicationService.deleteJobApplication(1L, 2L));
        verify(jobApplicationRepository, never()).deleteByIdAndUserId(any(), any());
    }
}
