package com.server.backend.common.auth.jwt.filter;

import com.server.backend.common.auth.jwt.util.JWTUtil;
import com.server.backend.common.auth.security.auth.CustomUserDetails;
import com.server.backend.common.data.entity.UserEntity;
import com.server.backend.common.data.enums.UserRoleType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    /**
     * 스프링 시큐리티 filter chain에 요청에 담긴 JWT를 검증하기 위한 커스텀 필터
     * 해당 필터를 통해 요청 헤더 Authorization 키에 JWT가 존재하는 경우 JWT를 검증하고 강제로 SecurityContextHolder에 세션을 생성
     * (이 세션은 STATELESS 상태로 관리되기 때문에 해당 요청이 끝나면 소멸)
     */

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        // 헤더에서 access 키에 담긴 토큰 꺼냄
//        String accessToken = request.getHeader("access");
//
//        // 토큰이 없다면 다음 필터로 넘김
//        if(accessToken == null) {
//            filterChain.doFilter(request,response);
//            return;
//        }

        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer")) {
            log.info("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split(" ")[1];

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if(!category.equals("access")) {
            // response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(username);
        userEntity.setUserRole(UserRoleType.ROLE_USER); // TODO 각 사용자별 권한이 들어가도록 처리해야함

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
