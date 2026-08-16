package com.jobtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtrack.dto.request.LoginRequest;
import com.jobtrack.dto.request.RegisterRequest;
import com.jobtrack.dto.response.AuthResponse;
import com.jobtrack.dto.response.UserProfileResponse;
import com.jobtrack.exception.BadRequestException;
import com.jobtrack.security.CustomUserDetailsService;
import com.jobtrack.security.JwtAuthenticationEntryPoint;
import com.jobtrack.security.JwtAuthenticationFilter;
import com.jobtrack.security.JwtTokenProvider;
import com.jobtrack.security.UserPrincipal;
import com.jobtrack.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("POST /api/v1/auth/register - Should register user successfully")
    void testRegister_Success() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .email("new@example.com")
                .password("password123")
                .fullName("New User")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("test.jwt.token")
                .tokenType("Bearer")
                .id(1L)
                .username("newuser")
                .email("new@example.com")
                .fullName("New User")
                .role("ROLE_USER")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.accessToken").value("test.jwt.token"))
                .andExpect(jsonPath("$.data.username").value("newuser"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 400 on duplicate username")
    void testRegister_Duplicate() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("existing")
                .email("test@example.com")
                .password("password123")
                .fullName("Existing User")
                .build();

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new BadRequestException("Username 'existing' is already taken"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Username 'existing' is already taken"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should authenticate user and return token")
    void testLogin_Success() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .usernameOrEmail("testuser")
                .password("password123")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("jwt.token.abc")
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role("ROLE_USER")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("jwt.token.abc"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 401 on invalid credentials")
    void testLogin_BadCredentials() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .usernameOrEmail("testuser")
                .password("wrongpassword")
                .build();

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/auth/me - Should return user profile for authenticated principal")
    void testGetCurrentUser() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(
                1L,
                "alex",
                "alex@example.com",
                "Alex Hunter",
                "secret",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        UserProfileResponse profileResponse = UserProfileResponse.builder()
                .id(1L)
                .username("alex")
                .email("alex@example.com")
                .fullName("Alex Hunter")
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();

        when(authService.getCurrentUserProfile(1L)).thenReturn(profileResponse);

        org.springframework.security.authentication.UsernamePasswordAuthenticationToken auth =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities());

        mockMvc.perform(get("/api/v1/auth/me")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("alex"))
                .andExpect(jsonPath("$.data.email").value("alex@example.com"));
    }
}
