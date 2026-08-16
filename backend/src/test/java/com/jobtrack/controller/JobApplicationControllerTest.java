package com.jobtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtrack.dto.request.JobApplicationRequest;
import com.jobtrack.dto.response.JobApplicationResponse;
import com.jobtrack.entity.enums.ApplicationStatus;
import com.jobtrack.entity.enums.EmploymentType;
import com.jobtrack.entity.enums.WorkplaceType;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.security.CustomUserDetailsService;
import com.jobtrack.security.JwtAuthenticationEntryPoint;
import com.jobtrack.security.JwtAuthenticationFilter;
import com.jobtrack.security.JwtTokenProvider;
import com.jobtrack.service.JobApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobApplicationService jobApplicationService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("POST /api/v1/jobs - Should create job application")
    void testCreateJob_Success() throws Exception {
        JobApplicationRequest request = JobApplicationRequest.builder()
                .companyName("Microsoft")
                .jobTitle("Senior Backend Engineer")
                .jobLocation("Hyderabad, India")
                .workplaceType(WorkplaceType.HYBRID)
                .employmentType(EmploymentType.FULL_TIME)
                .applicationStatus(ApplicationStatus.APPLIED)
                .salaryMin(new BigDecimal("3000000.00"))
                .salaryMax(new BigDecimal("4500000.00"))
                .salaryCurrency("INR")
                .appliedDate(LocalDate.now())
                .priority(5)
                .build();

        JobApplicationResponse response = JobApplicationResponse.builder()
                .id(1L)
                .companyName("Microsoft")
                .jobTitle("Senior Backend Engineer")
                .jobLocation("Hyderabad, India")
                .workplaceType(WorkplaceType.HYBRID)
                .employmentType(EmploymentType.FULL_TIME)
                .applicationStatus(ApplicationStatus.APPLIED)
                .salaryMin(new BigDecimal("3000000.00"))
                .salaryMax(new BigDecimal("4500000.00"))
                .salaryCurrency("INR")
                .appliedDate(LocalDate.now())
                .priority(5)
                .priorityLabel("Very High")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(jobApplicationService.createJobApplication(any(JobApplicationRequest.class), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.companyName").value("Microsoft"))
                .andExpect(jsonPath("$.data.salaryCurrency").value("INR"))
                .andExpect(jsonPath("$.data.priority").value(5))
                .andExpect(jsonPath("$.data.priorityLabel").value("Very High"));
    }

    @Test
    @DisplayName("POST /api/v1/jobs - Should return 400 when required fields are missing")
    void testCreateJob_ValidationError() throws Exception {
        JobApplicationRequest invalidRequest = JobApplicationRequest.builder()
                .companyName("") // Blank company name
                .jobTitle("")    // Blank job title
                .priority(10)    // Invalid priority > 5
                .build();

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.fieldErrors.companyName").exists())
                .andExpect(jsonPath("$.fieldErrors.jobTitle").exists())
                .andExpect(jsonPath("$.fieldErrors.priority").exists());
    }

    @Test
    @DisplayName("GET /api/v1/jobs - Should return all jobs")
    void testGetAllJobs() throws Exception {
        JobApplicationResponse response = JobApplicationResponse.builder()
                .id(1L)
                .companyName("Amazon")
                .jobTitle("SDE II")
                .salaryCurrency("INR")
                .priority(3)
                .priorityLabel("Medium")
                .build();

        when(jobApplicationService.getAllJobApplications(any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].companyName").value("Amazon"));
    }

    @Test
    @DisplayName("GET /api/v1/jobs/{id} - Should return job when found")
    void testGetJobById_Found() throws Exception {
        JobApplicationResponse response = JobApplicationResponse.builder()
                .id(1L)
                .companyName("Amazon")
                .jobTitle("SDE II")
                .salaryCurrency("INR")
                .priority(3)
                .priorityLabel("Medium")
                .build();

        when(jobApplicationService.getJobApplicationById(eq(1L), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.companyName").value("Amazon"));
    }

    @Test
    @DisplayName("GET /api/v1/jobs/{id} - Should return 404 when not found")
    void testGetJobById_NotFound() throws Exception {
        when(jobApplicationService.getJobApplicationById(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("JobApplication", "id", 99L));

        mockMvc.perform(get("/api/v1/jobs/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("DELETE /api/v1/jobs/{id} - Should delete job")
    void testDeleteJob() throws Exception {
        doNothing().when(jobApplicationService).deleteJobApplication(eq(1L), any());

        mockMvc.perform(delete("/api/v1/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/jobs/export/csv - Should return CSV file")
    void testExportJobsCsv() throws Exception {
        JobApplicationResponse response = JobApplicationResponse.builder()
                .id(1L)
                .companyName("Amazon")
                .jobTitle("SDE II")
                .jobLocation("Seattle, WA")
                .applicationStatus(ApplicationStatus.APPLIED)
                .salaryCurrency("USD")
                .priority(3)
                .build();

        when(jobApplicationService.getAllJobApplications(any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/jobs/export/csv"))
                .andExpect(status().isOk())
                .andExpect(status().isOk());
    }
}
