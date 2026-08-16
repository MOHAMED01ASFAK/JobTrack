package com.jobtrack.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String testSecret = "TestSecretKeyForJobTrackMustBeAtLeast256BitsLongForHmacSha256SecurityCompliance2026!";
    private final long testExpirationMs = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(testSecret, testExpirationMs);
    }

    @Test
    @DisplayName("Should generate and validate JWT token successfully")
    void testGenerateAndValidateToken() {
        UserPrincipal userPrincipal = new UserPrincipal(
                1L,
                "johndoe",
                "john@example.com",
                "John Doe",
                "password123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtTokenProvider.generateTokenFromUserPrincipal(userPrincipal);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("johndoe", jwtTokenProvider.getUsernameFromJwt(token));
        assertEquals(1L, jwtTokenProvider.getUserIdFromJwt(token));
    }

    @Test
    @DisplayName("Should return false for malformed or invalid token")
    void testValidateToken_Invalid() {
        assertFalse(jwtTokenProvider.validateToken("invalid.jwt.token"));
        assertFalse(jwtTokenProvider.validateToken(""));
        assertFalse(jwtTokenProvider.validateToken(null));
    }

    @Test
    @DisplayName("Should return false for expired token")
    void testValidateToken_Expired() {
        // Provider with -1000ms expiration (already expired)
        JwtTokenProvider expiredProvider = new JwtTokenProvider(testSecret, -1000L);

        UserPrincipal userPrincipal = new UserPrincipal(
                1L,
                "janedoe",
                "jane@example.com",
                "Jane Doe",
                "password123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String expiredToken = expiredProvider.generateTokenFromUserPrincipal(userPrincipal);
        assertFalse(jwtTokenProvider.validateToken(expiredToken));
    }
}
