package com.server.backend.common.auth.hadler;

import com.server.backend.common.auth.api.service.JWTService;
import com.server.backend.common.auth.jwt.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("LoginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("## LoginSuccessHandler onAuthenticationSuccess");

        // username, role
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // JWT(Access/Refresh) 발급
        String accessToken = jwtUtil.createJwt(username, role, true);
        String refreshToken = jwtUtil.createJwt(username, role, false);

        // 발급한 Refresh DB 테이블 저장 (Refresh whitelist)
        jwtService.addRefresh(username, refreshToken);

        // 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

//        String json = String.format("{\"accessToken\":\"%s\", \"refreshToken\":\"%s\"}", accessToken, refreshToken);
        String json = String.format("{\"accessToken\":\"%s\"", accessToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.getWriter().write(json);
        response.getWriter().flush();
        response.addCookie(cookie);
    }
}
