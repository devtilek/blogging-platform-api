package com.bloggingplatformapi.security;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceTest {

    private static final String SECRET = Base64.getEncoder().encodeToString(
            "this-is-a-very-long-test-secret-key-256".getBytes()
    );

    @Test
    void generatedToken_shouldContainUserEmail() {
        JwtService jwtService = new JwtService(SECRET, 3_600_000);

        String token = jwtService.generateToken("user@example.com", "USER");

        assertEquals("user@example.com", jwtService.extractEmail(token));
    }
}
