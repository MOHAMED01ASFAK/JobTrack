package com.jobtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtrack.dto.request.FollowUpRequest;
import com.jobtrack.dto.response.FollowUpResponse;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.security.CustomUserDetailsService;
import com.jobtrack.security.JwtAuthenticationEntryPoint;
import com.jobtrack.security.JwtAuthenticationFilter;
import com.jobtrack.security.JwtTokenProvider;
import com.jobtrack.service.FollowUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowUpController.class)
@AutoConfigureMockMvc(addFilters = false)
class FollowUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FollowUpService followUpService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("POST /api/v1/jobs/{jobId}/follow-ups - Should create follow-up")
    void testCreateFollowUp_Success() throws Exception {
        FollowUpRequest request = FollowUpRequest.builder()
                .dueDate(LocalDate.now().plusDays(5))
                .contactName("Sarah Smith")
                .contactEmail("sarah@company.com")
                .notes("Check in regarding status update")
                .isCompleted(false)
                .build();

        FollowUpResponse response = FollowUpResponse.builder()
                .id(1L)
                .jobApplicationId(5L)
                .dueDate(LocalDate.now().plusDays(5))
                .contactName("Sarah Smith")
                .contactEmail("sarah@company.com")
                .notes("Check in regarding status update")
                .isCompleted(false)
                .build();

        when(followUpService.createFollowUp(eq(5L), any(FollowUpRequest.class), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/jobs/5/follow-ups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.contactName").value("Sarah Smith"))
                .andExpect(jsonPath("$.data.contactEmail").value("sarah@company.com"))
                .andExpect(jsonPath("$.data.isCompleted").value(false));
    }

    @Test
    @DisplayName("POST /api/v1/jobs/{jobId}/follow-ups - Should return 400 when validation fails")
    void testCreateFollowUp_ValidationError() throws Exception {
        FollowUpRequest invalidRequest = FollowUpRequest.builder()
                .dueDate(null) // null due date
                .contactEmail("invalid-email-address") // invalid email
                .build();

        mockMvc.perform(post("/api/v1/jobs/5/follow-ups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.fieldErrors.dueDate").exists())
                .andExpect(jsonPath("$.fieldErrors.contactEmail").exists());
    }

    @Test
    @DisplayName("GET /api/v1/follow-ups - Should return all follow-ups")
    void testGetAllFollowUps_Success() throws Exception {
        FollowUpResponse response = FollowUpResponse.builder()
                .id(1L)
                .jobApplicationId(5L)
                .dueDate(LocalDate.now().plusDays(5))
                .contactName("Sarah Smith")
                .isCompleted(false)
                .build();

        when(followUpService.getAllFollowUps(any(), any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/follow-ups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].contactName").value("Sarah Smith"));
    }

    @Test
    @DisplayName("GET /api/v1/follow-ups?completed=false - Should return filtered follow-ups")
    void testGetFilteredFollowUps_Success() throws Exception {
        FollowUpResponse response = FollowUpResponse.builder()
                .id(1L)
                .jobApplicationId(5L)
                .dueDate(LocalDate.now().plusDays(5))
                .contactName("Sarah Smith")
                .isCompleted(false)
                .build();

        when(followUpService.getAllFollowUps(any(), eq(false))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/follow-ups?completed=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].isCompleted").value(false));
    }

    @Test
    @DisplayName("PATCH /api/v1/follow-ups/{id}/toggle - Should toggle completion")
    void testToggleFollowUp_Success() throws Exception {
        FollowUpResponse response = FollowUpResponse.builder()
                .id(1L)
                .isCompleted(true)
                .build();

        when(followUpService.toggleFollowUpCompletion(eq(1L), any())).thenReturn(response);

        mockMvc.perform(patch("/api/v1/follow-ups/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isCompleted").value(true));
    }

    @Test
    @DisplayName("PATCH /api/v1/follow-ups/{id}/toggle - Should return 404 when not found")
    void testToggleFollowUp_NotFound() throws Exception {
        when(followUpService.toggleFollowUpCompletion(eq(999L), any()))
                .thenThrow(new ResourceNotFoundException("FollowUp", "id", 999L));

        mockMvc.perform(patch("/api/v1/follow-ups/999/toggle"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
