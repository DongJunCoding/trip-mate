package com.server.backend.common.auth.api.controller;

import com.server.backend.common.auth.api.service.JWTService;
import com.server.backend.common.auth.dto.JWTTokenDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/jwt")
public class JWTController {

    private final JWTService jwtService;

    // 소셜 로그인 쿠키 방식의 Refresh 토큰 헤더 방식으로 교환
    @PostMapping(value = "/exchange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JWTTokenDTO exchange(HttpServletRequest request, HttpServletResponse response) {
        log.info("## JWTController exchange");

        return jwtService.cookie2Header(request, response);
    }

    // Refresh 토큰으로 Access 토큰 재발급 (Rotate 포함)
    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JWTTokenDTO refresh(HttpServletRequest request, HttpServletResponse response) {
        log.info("## JWTController refresh");

        String refreshToken = null;

        for(Cookie cookie : request.getCookies()) {
            if("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        JWTTokenDTO jwtTokenDTO = jwtService.refreshRotate(refreshToken);

        Cookie newCookie = new Cookie("refreshToken", jwtTokenDTO.refreshToken());
        newCookie.setHttpOnly(true);
        newCookie.setSecure(false); // 배포 시 true
        newCookie.setPath("/");
        newCookie.setMaxAge(60 * 60 * 24);

        response.addCookie(newCookie);

        return jwtTokenDTO;
    }
}
