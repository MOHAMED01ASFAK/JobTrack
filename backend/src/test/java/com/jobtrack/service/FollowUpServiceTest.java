package com.jobtrack.service;

import com.jobtrack.dto.request.FollowUpRequest;
import com.jobtrack.dto.response.FollowUpResponse;
import com.jobtrack.entity.FollowUp;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.User;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.mapper.FollowUpMapper;
import com.jobtrack.repository.FollowUpRepository;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.impl.FollowUpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowUpServiceTest {

    @Mock
    private FollowUpRepository followUpRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Spy
    private FollowUpMapper followUpMapper = new FollowUpMapper();

    @InjectMocks
    private FollowUpServiceImpl followUpService;

    private User sampleUser;
    private JobApplication sampleJob;
    private FollowUp sampleFollowUp;
    private FollowUpRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();
        sampleUser.setId(1L);

        sampleJob = JobApplication.builder()
                .user(sampleUser)
                .companyName("Uber")
                .jobTitle("Software Engineer II")
                .build();
        sampleJob.setId(20L);

        sampleFollowUp = FollowUp.builder()
                .jobApplication(sampleJob)
                .dueDate(LocalDate.now().plusDays(3))
                .contactName("Alex Recruiter")
                .contactEmail("alex@uber.com")
                .notes("Follow up regarding interview feedback")
                .isCompleted(false)
                .build();
        sampleFollowUp.setId(200L);

        sampleRequest = FollowUpRequest.builder()
                .dueDate(LocalDate.now().plusDays(3))
                .contactName("Alex Recruiter")
                .contactEmail("alex@uber.com")
                .notes("Follow up regarding interview feedback")
                .isCompleted(false)
                .build();
    }

    @Test
    @DisplayName("Should create follow-up successfully under user's job")
    void testCreateFollowUp_Success() {
        when(jobApplicationRepository.findByIdAndUserId(20L, 1L)).thenReturn(Optional.of(sampleJob));
        when(followUpRepository.save(any(FollowUp.class))).thenReturn(sampleFollowUp);

        FollowUpResponse response = followUpService.createFollowUp(20L, sampleRequest, 1L);

        assertNotNull(response);
        assertEquals(200L, response.getId());
        assertEquals(20L, response.getJobApplicationId());
        assertEquals("Alex Recruiter", response.getContactName());
        assertEquals("alex@uber.com", response.getContactEmail());
        assertFalse(response.isCompleted());

        verify(followUpRepository, times(1)).save(any(FollowUp.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when creating follow-up under another user's job")
    void testCreateFollowUp_JobNotFound() {
        when(jobApplicationRepository.findByIdAndUserId(20L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> followUpService.createFollowUp(20L, sampleRequest, 2L));
        verify(followUpRepository, never()).save(any(FollowUp.class));
    }

    @Test
    @DisplayName("Should retrieve all follow-ups for user without filter")
    void testGetAllFollowUps_All() {
        when(followUpRepository.findAllByJobApplicationUserIdOrderByDueDateAsc(1L)).thenReturn(List.of(sampleFollowUp));

        List<FollowUpResponse> responses = followUpService.getAllFollowUps(1L, null);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Alex Recruiter", responses.get(0).getContactName());
    }

    @Test
    @DisplayName("Should retrieve filtered follow-ups for user when completed=false")
    void testGetAllFollowUps_FilteredPending() {
        when(followUpRepository.findAllByJobApplicationUserIdAndIsCompletedOrderByDueDateAsc(1L, false))
                .thenReturn(List.of(sampleFollowUp));

        List<FollowUpResponse> responses = followUpService.getAllFollowUps(1L, false);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertFalse(responses.get(0).isCompleted());
    }

    @Test
    @DisplayName("Should toggle follow-up completion status from false to true")
    void testToggleFollowUpCompletion_Success() {
        when(followUpRepository.findByIdAndJobApplicationUserId(200L, 1L)).thenReturn(Optional.of(sampleFollowUp));
        when(followUpRepository.save(any(FollowUp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FollowUpResponse response = followUpService.toggleFollowUpCompletion(200L, 1L);

        assertNotNull(response);
        assertTrue(response.isCompleted());
        verify(followUpRepository, times(1)).save(sampleFollowUp);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User B tries to toggle User A's follow-up")
    void testToggleFollowUpCompletion_UserIsolationForbidden() {
        when(followUpRepository.findByIdAndJobApplicationUserId(200L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> followUpService.toggleFollowUpCompletion(200L, 2L));
        verify(followUpRepository, never()).save(any(FollowUp.class));
    }

    @Test
    @DisplayName("Should delete follow-up successfully for owner user")
    void testDeleteFollowUp_Success() {
        when(followUpRepository.existsByIdAndJobApplicationUserId(200L, 1L)).thenReturn(true);
        doNothing().when(followUpRepository).deleteByIdAndJobApplicationUserId(200L, 1L);

        assertDoesNotThrow(() -> followUpService.deleteFollowUp(200L, 1L));
        verify(followUpRepository, times(1)).deleteByIdAndJobApplicationUserId(200L, 1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User B tries to delete User A's follow-up")
    void testDeleteFollowUp_UserIsolationForbidden() {
        when(followUpRepository.existsByIdAndJobApplicationUserId(200L, 2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> followUpService.deleteFollowUp(200L, 2L));
        verify(followUpRepository, never()).deleteByIdAndJobApplicationUserId(any(), any());
    }
}
