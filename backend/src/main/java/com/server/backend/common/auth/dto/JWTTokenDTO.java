package com.server.backend.common.auth.dto;

public record JWTTokenDTO (String accessToken, String refreshToken) {
}
