package com.server.backend.common.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTRefreshRequestDTO {

    @NotBlank
    private String refreshToken;
}
