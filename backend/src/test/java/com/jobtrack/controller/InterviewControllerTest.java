package com.jobtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtrack.dto.request.InterviewRequest;
import com.jobtrack.dto.response.InterviewResponse;
import com.jobtrack.entity.enums.InterviewRoundType;
import com.jobtrack.entity.enums.InterviewStatus;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.security.CustomUserDetailsService;
import com.jobtrack.security.JwtAuthenticationEntryPoint;
import com.jobtrack.security.JwtAuthenticationFilter;
import com.jobtrack.security.JwtTokenProvider;
import com.jobtrack.service.InterviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InterviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class InterviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InterviewService interviewService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("POST /api/v1/jobs/{jobId}/interviews - Should schedule interview")
    void testScheduleInterview_Success() throws Exception {
        InterviewRequest request = InterviewRequest.builder()
                .roundName("System Design Round")
                .roundType(InterviewRoundType.SYSTEM_DESIGN)
                .scheduledTime(LocalDateTime.now().plusDays(2))
                .meetingLink("https://meet.google.com/xyz")
                .build();

        InterviewResponse response = InterviewResponse.builder()
                .id(1L)
                .jobApplicationId(10L)
                .roundName("System Design Round")
                .roundType(InterviewRoundType.SYSTEM_DESIGN)
                .scheduledTime(LocalDateTime.now().plusDays(2))
                .status(InterviewStatus.SCHEDULED)
                .build();

        when(interviewService.scheduleInterview(eq(10L), any(InterviewRequest.class), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/jobs/10/interviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.roundName").value("System Design Round"))
                .andExpect(jsonPath("$.data.roundType").value("SYSTEM_DESIGN"));
    }

    @Test
    @DisplayName("POST /api/v1/jobs/{jobId}/interviews - Should return 400 when validation fails")
    void testScheduleInterview_ValidationError() throws Exception {
        InterviewRequest invalidRequest = InterviewRequest.builder()
                .roundName("") // blank
                .roundType(null) // null
                .scheduledTime(null) // null
                .build();

        mockMvc.perform(post("/api/v1/jobs/10/interviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.fieldErrors.roundName").exists())
                .andExpect(jsonPath("$.fieldErrors.roundType").exists())
                .andExpect(jsonPath("$.fieldErrors.scheduledTime").exists());
    }

    @Test
    @DisplayName("GET /api/v1/jobs/{jobId}/interviews - Should return interviews")
    void testGetInterviewsByJobId_Success() throws Exception {
        InterviewResponse response = InterviewResponse.builder()
                .id(1L)
                .jobApplicationId(10L)
                .roundName("HR Screen")
                .roundType(InterviewRoundType.HR)
                .status(InterviewStatus.SCHEDULED)
                .build();

        when(interviewService.getInterviewsByJobId(eq(10L), any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/jobs/10/interviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].roundName").value("HR Screen"));
    }

    @Test
    @DisplayName("PUT /api/v1/interviews/{id} - Should update interview")
    void testUpdateInterview_Success() throws Exception {
        InterviewRequest request = InterviewRequest.builder()
                .roundName("Managerial Round")
                .roundType(InterviewRoundType.MANAGERIAL)
                .scheduledTime(LocalDateTime.now().plusDays(1))
                .status(InterviewStatus.COMPLETED)
                .build();

        InterviewResponse response = InterviewResponse.builder()
                .id(1L)
                .roundName("Managerial Round")
                .roundType(InterviewRoundType.MANAGERIAL)
                .status(InterviewStatus.COMPLETED)
                .build();

        when(interviewService.updateInterview(eq(1L), any(InterviewRequest.class), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/interviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("DELETE /api/v1/interviews/{id} - Should delete interview")
    void testDeleteInterview_Success() throws Exception {
        doNothing().when(interviewService).deleteInterview(eq(1L), any());

        mockMvc.perform(delete("/api/v1/interviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("PUT /api/v1/interviews/{id} - Should return 404 when not found")
    void testUpdateInterview_NotFound() throws Exception {
        InterviewRequest request = InterviewRequest.builder()
                .roundName("Behavioral Round")
                .roundType(InterviewRoundType.BEHAVIORAL)
                .scheduledTime(LocalDateTime.now().plusDays(1))
                .build();

        when(interviewService.updateInterview(eq(999L), any(InterviewRequest.class), any()))
                .thenThrow(new ResourceNotFoundException("Interview", "id", 999L));

        mockMvc.perform(put("/api/v1/interviews/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
