package com.bloggingplatformapi.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        String email,
        String role
) {
}
