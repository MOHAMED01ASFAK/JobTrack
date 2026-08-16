package com.jobtrack.controller;

import com.jobtrack.dto.request.LoginRequest;
import com.jobtrack.dto.request.RegisterRequest;
import com.jobtrack.dto.response.ApiResponse;
import com.jobtrack.dto.response.AuthResponse;
import com.jobtrack.dto.response.UserProfileResponse;
import com.jobtrack.exception.UnauthorizedException;
import com.jobtrack.security.UserPrincipal;
import com.jobtrack.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing user authentication, registration, and profile endpoints under /api/v1/auth.
 */
@Tag(name = "Authentication", description = "User registration, authentication login, and profile operations")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user account.
     * POST /api/v1/auth/register
     */
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with BCrypt password encryption and returns an initial JWT access token."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User successfully registered"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload or duplicate username/email")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("REST request to register new user: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully", response),
                HttpStatus.CREATED
        );
    }

    /**
     * Authenticate existing user and return JWT access token.
     * POST /api/v1/auth/login
     */
    @Operation(
            summary = "Authenticate user & obtain JWT token",
            description = "Authenticates user credentials against the database and returns a signed JWT access token with user details."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Authentication successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid login payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Bad credentials / invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("REST request to authenticate user: {}", request.getUsernameOrEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Authentication successful", response));
    }

    /**
     * Get profile of currently authenticated user.
     * GET /api/v1/auth/me
     */
    @Operation(
            summary = "Get current authenticated user profile",
            description = "Retrieves profile information of the currently authenticated user based on the provided Bearer JWT token.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Missing or expired JWT token")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            org.springframework.security.core.Authentication authentication) {
        Long userId = null;
        if (userPrincipal != null) {
            userId = userPrincipal.getId();
        } else if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            userId = ((UserPrincipal) authentication.getPrincipal()).getId();
        }

        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        log.info("REST request to get current user profile for ID: {}", userId);
        UserProfileResponse response = authService.getCurrentUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", response));
    }
}
