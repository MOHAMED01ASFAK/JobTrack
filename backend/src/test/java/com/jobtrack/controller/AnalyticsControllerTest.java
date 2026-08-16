package com.jobtrack.controller;

import com.jobtrack.dto.response.AnalyticsSummaryResponse;
import com.jobtrack.security.CustomUserDetailsService;
import com.jobtrack.security.JwtAuthenticationEntryPoint;
import com.jobtrack.security.JwtAuthenticationFilter;
import com.jobtrack.security.JwtTokenProvider;
import com.jobtrack.service.AnalyticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/analytics/summary - Should return analytics summary")
    void testGetAnalyticsSummary() throws Exception {
        AnalyticsSummaryResponse summary = AnalyticsSummaryResponse.builder()
                .totalApplications(10)
                .activeApplications(5)
                .interviewRatePercentage(60.0)
                .offerRatePercentage(20.0)
                .avgSalary(150000.0)
                .statusBreakdown(Collections.singletonMap("APPLIED", 4L))
                .build();

        when(analyticsService.getAnalyticsSummary(any())).thenReturn(summary);

        mockMvc.perform(get("/api/v1/analytics/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Analytics summary retrieved successfully"))
                .andExpect(jsonPath("$.data.totalApplications").value(10))
                .andExpect(jsonPath("$.data.interviewRatePercentage").value(60.0));
    }
}
