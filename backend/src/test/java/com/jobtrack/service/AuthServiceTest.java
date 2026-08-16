package com.jobtrack.service;

import com.jobtrack.dto.request.LoginRequest;
import com.jobtrack.dto.request.RegisterRequest;
import com.jobtrack.dto.response.AuthResponse;
import com.jobtrack.dto.response.UserProfileResponse;
import com.jobtrack.entity.User;
import com.jobtrack.entity.enums.Role;
import com.jobtrack.exception.BadRequestException;
import com.jobtrack.repository.UserRepository;
import com.jobtrack.security.JwtTokenProvider;
import com.jobtrack.security.UserPrincipal;
import com.jobtrack.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .username("john_dev")
                .email("john@example.com")
                .password("encoded_pass")
                .fullName("John Developer")
                .role(Role.ROLE_USER)
                .build();
        sampleUser.setId(1L);

        registerRequest = RegisterRequest.builder()
                .username("john_dev")
                .email("john@example.com")
                .password("password123")
                .fullName("John Developer")
                .build();

        loginRequest = LoginRequest.builder()
                .usernameOrEmail("john_dev")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("Should register new user successfully")
    void testRegister_Success() {
        when(userRepository.existsByUsername("john_dev")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(jwtTokenProvider.generateTokenFromUserPrincipal(any(UserPrincipal.class))).thenReturn("jwt.test.token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwt.test.token", response.getAccessToken());
        assertEquals("john_dev", response.getUsername());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("ROLE_USER", response.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when username already taken")
    void testRegister_DuplicateUsername() {
        when(userRepository.existsByUsername("john_dev")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when email already registered")
    void testRegister_DuplicateEmail() {
        when(userRepository.existsByUsername("john_dev")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should login existing user successfully")
    void testLogin_Success() {
        UserPrincipal principal = new UserPrincipal(
                1L,
                "john_dev",
                "john@example.com",
                "John Developer",
                "encoded_pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt.login.token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt.login.token", response.getAccessToken());
        assertEquals("john_dev", response.getUsername());
        assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("Should get user profile by ID")
    void testGetCurrentUserProfile_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserProfileResponse profile = authService.getCurrentUserProfile(1L);

        assertNotNull(profile);
        assertEquals(1L, profile.getId());
        assertEquals("john_dev", profile.getUsername());
        assertEquals("john@example.com", profile.getEmail());
        assertEquals("John Developer", profile.getFullName());
    }
}
