package com.server.backend.common.auth.api.controller;

import com.server.backend.common.auth.api.service.JWTService;
import com.server.backend.common.auth.dto.JWTRefreshRequestDTO;
import com.server.backend.common.auth.dto.JWTTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/jwt")
public class JWTController {

    private final JWTService jwtService;

    // 소셜 로그인 쿠키 방식의 Refresh 토큰 헤더 방식으로 교환
    @PostMapping(value = "/exchange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JWTTokenDTO exchange(HttpServletRequest request, HttpServletResponse response) {
        log.info("## UserController exchange");

        return jwtService.cookie2Header(request, response);
    }

    // Refresh 토큰으로 Access 토큰 재발급 (Rotate 포함)
    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JWTTokenDTO refresh(@Validated @RequestBody JWTRefreshRequestDTO dto) {
        log.info("## UserController refresh");

        return jwtService.refreshRotate(dto);
    }
}
