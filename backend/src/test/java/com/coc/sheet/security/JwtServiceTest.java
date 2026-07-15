package com.coc.sheet.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRET = "test-secret-key-that-is-at-least-32-bytes-long!!";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, 60);
    }

    @Test
    void generatesTokenThatEncodesUserIdAndIsValid() {
        String token = jwtService.generateToken("user-123", "kevin");

        assertTrue(jwtService.isValid(token));
        assertEquals("user-123", jwtService.extractUserId(token));
    }

    @Test
    void rejectsMalformedToken() {
        assertFalse(jwtService.isValid("not-a-real-jwt"));
    }

    @Test
    void expiredTokenIsInvalid() {
        JwtService alreadyExpired = new JwtService(SECRET, 0);
        String token = alreadyExpired.generateToken("user-123", "kevin");

        assertFalse(alreadyExpired.isValid(token));
    }
}
