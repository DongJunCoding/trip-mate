package com.server.backend.common.auth.dto;

public record UserResponseDTO(String userId, Boolean social, String nickname, String userEmail) {
}
