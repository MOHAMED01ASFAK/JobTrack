package com.jobtrack.service;

import com.jobtrack.dto.request.InterviewRequest;
import com.jobtrack.dto.response.InterviewResponse;
import com.jobtrack.entity.Interview;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.User;
import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.InterviewRoundType;
import com.jobtrack.entity.enums.InterviewStatus;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.mapper.InterviewMapper;
import com.jobtrack.repository.InterviewRepository;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.impl.InterviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Spy
    private InterviewMapper interviewMapper = new InterviewMapper();

    @InjectMocks
    private InterviewServiceImpl interviewService;

    private User sampleUser;
    private JobApplication sampleJob;
    private Interview sampleInterview;
    private InterviewRequest sampleRequest;

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
                .companyName("Stripe")
                .jobTitle("Backend Engineer")
                .applicationStatus(ApplicationStatus.INTERVIEWING)
                .build();
        sampleJob.setId(10L);

        sampleInterview = Interview.builder()
                .jobApplication(sampleJob)
                .roundName("Round 1 - Technical Screen")
                .roundType(InterviewRoundType.TECHNICAL)
                .scheduledTime(LocalDateTime.now().plusDays(2))
                .interviewerInfo("Jane Doe (Staff Engineer)")
                .meetingLink("https://meet.google.com/abc-defg-hij")
                .status(InterviewStatus.SCHEDULED)
                .questionsAsked("Data structure design, Concurrency in Java")
                .feedbackNotes("Candidate performed well on algorithms")
                .build();
        sampleInterview.setId(100L);

        sampleRequest = InterviewRequest.builder()
                .roundName("Round 1 - Technical Screen")
                .roundType(InterviewRoundType.TECHNICAL)
                .scheduledTime(LocalDateTime.now().plusDays(2))
                .interviewerInfo("Jane Doe (Staff Engineer)")
                .meetingLink("https://meet.google.com/abc-defg-hij")
                .status(InterviewStatus.SCHEDULED)
                .questionsAsked("Data structure design, Concurrency in Java")
                .feedbackNotes("Candidate performed well on algorithms")
                .build();
    }

    @Test
    @DisplayName("Should schedule interview successfully under user's job")
    void testScheduleInterview_Success() {
        when(jobApplicationRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(sampleJob));
        when(interviewRepository.save(any(Interview.class))).thenReturn(sampleInterview);

        InterviewResponse response = interviewService.scheduleInterview(10L, sampleRequest, 1L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(10L, response.getJobApplicationId());
        assertEquals("Round 1 - Technical Screen", response.getRoundName());
        assertEquals(InterviewRoundType.TECHNICAL, response.getRoundType());
        assertEquals(InterviewStatus.SCHEDULED, response.getStatus());

        verify(interviewRepository, times(1)).save(any(Interview.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when scheduling under non-existent job or another user's job")
    void testScheduleInterview_JobNotFound() {
        when(jobApplicationRepository.findByIdAndUserId(10L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> interviewService.scheduleInterview(10L, sampleRequest, 2L));
        verify(interviewRepository, never()).save(any(Interview.class));
    }

    @Test
    @DisplayName("Should retrieve all interviews for user's job")
    void testGetInterviewsByJobId_Success() {
        when(jobApplicationRepository.existsByIdAndUserId(10L, 1L)).thenReturn(true);
        when(interviewRepository.findByJobApplicationIdAndJobApplicationUserIdOrderByScheduledTimeAsc(10L, 1L))
                .thenReturn(List.of(sampleInterview));

        List<InterviewResponse> responses = interviewService.getInterviewsByJobId(10L, 1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Round 1 - Technical Screen", responses.get(0).getRoundName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when fetching interviews for another user's job")
    void testGetInterviewsByJobId_UserIsolationForbidden() {
        when(jobApplicationRepository.existsByIdAndUserId(10L, 2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> interviewService.getInterviewsByJobId(10L, 2L));
    }

    @Test
    @DisplayName("Should retrieve interview by ID for owner user")
    void testGetInterviewById_Success() {
        when(interviewRepository.findByIdAndJobApplicationUserId(100L, 1L)).thenReturn(Optional.of(sampleInterview));

        InterviewResponse response = interviewService.getInterviewById(100L, 1L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Round 1 - Technical Screen", response.getRoundName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User B accesses User A's interview")
    void testGetInterviewById_UserIsolationForbidden() {
        when(interviewRepository.findByIdAndJobApplicationUserId(100L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> interviewService.getInterviewById(100L, 2L));
    }

    @Test
    @DisplayName("Should update interview successfully for owner user")
    void testUpdateInterview_Success() {
        when(interviewRepository.findByIdAndJobApplicationUserId(100L, 1L)).thenReturn(Optional.of(sampleInterview));
        when(interviewRepository.save(any(Interview.class))).thenReturn(sampleInterview);

        sampleRequest.setRoundName("Round 2 - System Design");
        sampleRequest.setRoundType(InterviewRoundType.SYSTEM_DESIGN);
        sampleRequest.setStatus(InterviewStatus.COMPLETED);

        InterviewResponse response = interviewService.updateInterview(100L, sampleRequest, 1L);

        assertNotNull(response);
        verify(interviewRepository, times(1)).save(sampleInterview);
    }

    @Test
    @DisplayName("Should delete interview successfully for owner user")
    void testDeleteInterview_Success() {
        when(interviewRepository.existsByIdAndJobApplicationUserId(100L, 1L)).thenReturn(true);
        doNothing().when(interviewRepository).deleteByIdAndJobApplicationUserId(100L, 1L);

        assertDoesNotThrow(() -> interviewService.deleteInterview(100L, 1L));
        verify(interviewRepository, times(1)).deleteByIdAndJobApplicationUserId(100L, 1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User B tries to delete User A's interview")
    void testDeleteInterview_UserIsolationForbidden() {
        when(interviewRepository.existsByIdAndJobApplicationUserId(100L, 2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> interviewService.deleteInterview(100L, 2L));
        verify(interviewRepository, never()).deleteByIdAndJobApplicationUserId(any(), any());
    }
}
