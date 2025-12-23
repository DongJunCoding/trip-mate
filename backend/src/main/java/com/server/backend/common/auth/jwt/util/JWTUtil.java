package com.server.backend.common.auth.jwt.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey) // 가지고 있는 secretKey를 넣어서 token이 우리 서버에서 생성되었는지, 우리 서버에서 생성 된게 우리가 가지고 있는 key랑 맞는지 검증
                .build()
                .parseSignedClaims(token) // 클래임 확인
                .getPayload() // 페이로드 부분에서 특정 데이터 가져오기
                .get("username", String.class); // 특정 데이터를 get으로 가져오며 형식은 String으로 지정
    }

    public String getRole(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String getCategory(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String createJwt(String category, String username, String role, Long expiredMs) {
        return Jwts
                .builder()
                .claim("category", category)
                .claim("username", username) // payload 부분에 넣을 데이터
                .claim("role", role) // payload 부분에 넣을 데이터
                .issuedAt(new Date(System.currentTimeMillis())) // 현재 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 이 토큰이 언제 소멸될지 설정
                .signWith(secretKey) // 시크릿키를 통해 시그니처를 만들어 암호화 진행
                .compact(); // 토큰을 컴팩트 하여 리턴, 위에서 설정한 Header + Payload + Signature를 .으로 구분된 문자열로 압축
    }
}
