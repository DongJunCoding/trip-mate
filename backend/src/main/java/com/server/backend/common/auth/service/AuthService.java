package com.server.backend.common.auth.service;

import com.server.backend.common.auth.dto.UserDTO;
import com.server.backend.common.auth.jwt.util.JWTUtil;
import com.server.backend.common.data.entity.UserEntity;
import com.server.backend.common.data.entity.UserTokenEntity;
import com.server.backend.common.data.enums.SocialProviderType;
import com.server.backend.common.data.enums.UserRoleType;
import com.server.backend.common.data.repository.UserRepository;
import com.server.backend.common.data.repository.UserTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUp(UserDTO userDTO) {
        log.info("## AuthService signUp");

        userRepository.save(
                UserEntity.builder()
                        .userId(userDTO.getUserId())
                        .userPw(bCryptPasswordEncoder.encode(userDTO.getUserPw()))
                        .userEmail(userDTO.getUserEmail())
                        .userRole(UserRoleType.ROLE_USER)
                        .nickname(userDTO.getNickname())
                        .isLock(false) // 임시
                        .isSocial(false) // 임시
                        .socialProviderType(SocialProviderType.LOCAL) // 임시
                        .build()
        );
    }

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("## AuthService reissue");

        // get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if(refresh == null) {
            // response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);

        }

        // expired check
        try{
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인(발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if(!category.equals("refresh")) {
            // response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = userTokenRepository.existsByRefreshToken(refresh);
        if(!isExist) {
            // response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 6000000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        userTokenRepository.deleteByRefreshToken(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        // response
        response.setHeader("Authorization","Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        log.info("## AuthService createCookie");

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        // cookie.setSecure(true);
        // cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        log.info("## AuthService addRefreshEntity");

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        UserTokenEntity userTokenEntity = new UserTokenEntity();
        userTokenEntity.setUserId(username);
        userTokenEntity.setRefreshToken(refresh);
        userTokenEntity.setExpiration(date.toString());

        userTokenRepository.save(userTokenEntity);
    }
}
