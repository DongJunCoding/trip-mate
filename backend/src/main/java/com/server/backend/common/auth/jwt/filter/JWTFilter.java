package com.server.backend.common.auth.jwt.filter;

import com.server.backend.common.auth.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    // 스프링 시큐리티 filter chain에 요청에 담긴 JWT를 검증하기 위한 커스텀 필터
    // 해당 필터를 통해 요청 헤더 Authorization 키에 JWT가 존재하는 경우 JWT를 검증하고 강제로 SecurityContextHolder에 세션을 생성
    // (이 세션은 STATELESS 상태로 관리되기 때문에 해당 요청이 끝나면 소멸)

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
