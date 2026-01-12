package com.server.backend.common.auth.api.service;

import com.server.backend.common.auth.dto.JWTRefreshRequestDTO;
import com.server.backend.common.auth.dto.JWTTokenDTO;
import com.server.backend.common.auth.jwt.util.JWTUtil;
import com.server.backend.common.data.entity.UserTokenEntity;
import com.server.backend.common.data.repository.UserTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTService {

    private final JWTUtil jwtUtil;
    private final UserTokenRepository userTokenRepository;

    // 소셜 로그인 성공 후 쿠키(Refresh -> 헤더 방식으로 응답
    @Transactional
    public JWTTokenDTO cookie2Header(HttpServletRequest request, HttpServletResponse response) {
        log.info("## JWTService cookie2Header");

        // 쿠키 리스트
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            throw new RuntimeException("쿠키가 존재하지 않습니다.");
        }

        // RefreshToken 획득
        String refreshToken = null;
        for(Cookie cookie : cookies) {
            if("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if(refreshToken == null) {
            throw new RuntimeException("refrehsToken 쿠키가 없습니다.");
        }

        // RefreshToken 검증
        Boolean isValid = jwtUtil.isValid(refreshToken, false);
        if(!isValid) {
            throw new RuntimeException("유효하지 않은 refreshToken입니다.");
        }

        // 정보 추출
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 토큰 생성
        String newAccessToken = jwtUtil.createJwt(username, role, true);
        String newRefreshToken = jwtUtil.createJwt(username, role, false);

        // 기존 RefreshToken DB 삭제 후 신규 추가
        UserTokenEntity userTokenEntity = UserTokenEntity.builder()
                .userId(username)
                .refreshToken(newRefreshToken)
                .build();

        deleteRefresh(refreshToken);
        userTokenRepository.flush(); // 같은 트랜잭션 내부라 : 삭제 -> 생성 문제 해결
        userTokenRepository.save(userTokenEntity);

        // 기존 쿠키 제거
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(10);
        response.addCookie(refreshCookie);

        return new JWTTokenDTO(newAccessToken, newRefreshToken);
    }



    // Refresh 토큰으로 Access 토큰 재발급 로직 (Rotate 포함)
    @Transactional
    public JWTTokenDTO refreshRotate(JWTRefreshRequestDTO jwtRefreshRequestDTO) {
        log.info("## JWTService refreshRotate");

        String refreshToken = jwtRefreshRequestDTO.getRefreshToken();

        // RefreshToken 검증
        Boolean isValid = jwtUtil.isValid(refreshToken, false);
        if(!isValid) {
            throw new RuntimeException("유효하지 않은 refreshToken 입니다.");
        }

        // RefreshEntity 존재 확인 (화이트리스트)
        if(!existsRefresh(refreshToken)) {
            throw new RuntimeException("유효하지 않은 refreshToken 입니다.");
        }

        // 정보 추출
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 토큰 생성
        String newAccessToken = jwtUtil.createJwt(username, role, true);
        String newRefreshToken = jwtUtil.createJwt(username, role, false);

        // 기존 Refresh 토큰 DB 삭제 후 신규 추가
        UserTokenEntity userTokenEntity = UserTokenEntity.builder()
                .userId(username)
                .refreshToken(newRefreshToken)
                .build();

        deleteRefresh(refreshToken);
        userTokenRepository.save(userTokenEntity);

        return new JWTTokenDTO(newAccessToken, newRefreshToken);
    }



    // JWT Refresh 토큰 발급 후 저장 메소드
    @Transactional
    public void addRefresh(String username, String refreshToken) {
        log.info("## JWTService addRefresh");

        UserTokenEntity userTokenEntity = UserTokenEntity.builder()
                .userId(username)
                .refreshToken(refreshToken)
                .build();

        userTokenRepository.save(userTokenEntity);
    }



    // JWT Refresh 존재 확인 메소드
    @Transactional(readOnly = true)
    public Boolean existsRefresh(String refreshToken) {
        return userTokenRepository.existsByRefreshToken(refreshToken);
    }



    // JWT Refresh 토큰 삭제 메소드
    @Transactional
    public void deleteRefresh(String refreshToken) {
        userTokenRepository.deleteByRefreshToken(refreshToken);
    }



    // 특정 유저 Refresh 토큰 모두 삭제 (탈퇴)
    @Transactional
    void removeRefreshUser(String username) {
        userTokenRepository.deleteByUserId(username);
    }



    // Refresh 토큰 저장소 8일 지난 토큰 삭제
    @Transactional
    public void refreshEntityTtlSchedule() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(8);
        userTokenRepository.deleteByAddedDateBefore(cutoff);
    }
}
