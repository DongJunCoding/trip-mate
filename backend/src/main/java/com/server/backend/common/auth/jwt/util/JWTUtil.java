package com.server.backend.common.auth.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final Long accessExpirationMs;
    private final Long refreshExpirationMs;

    public JWTUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration-ms}") Long accessExpirationMs,
            @Value("${jwt.refresh-expiration-ms}") Long refreshExpirationMs
    ) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    // JWT 클레임 username 파싱
    public String getUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey) // 가지고 있는 secretKey를 넣어서 token이 우리 서버에서 생성되었는지, 우리 서버에서 생성 된게 우리가 가지고 있는 key랑 맞는지 검증
                .build()
                .parseSignedClaims(token) // 클래임 확인
                .getPayload() // 페이로드 부분에서 특정 데이터 가져오기
                .get("sub", String.class); // 특정 데이터를 get으로 가져오며 형식은 String으로 지정
    }

    // JWT 클레임 role 파싱
    public String getRole(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // JWT 유효 여부(위조, 시간, Access/Refresh 여부)
    public Boolean isValid(String token, Boolean isAccess) {
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = claims.get("type", String.class);
            if(type == null) return false;

            if(isAccess && !type.equals("access")) return false;
            if(!isAccess && !type.equals("refresh")) return false;

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // JWT(Access/Refresh) 생성
    public String createJwt(String username, String role, Boolean isAccess) {

        long now = System.currentTimeMillis();
        long expiry = isAccess ? accessExpirationMs : refreshExpirationMs;
        String type = isAccess ? "access" : "refresh";

        return Jwts
                .builder()
                .claim("sub", username) // payload 부분에 넣을 데이터
                .claim("role", role) // payload 부분에 넣을 데이터
                .claim("type", type) // payload 부분에 넣을 데이터
                .issuedAt(new Date(now)) // 현재 발행 시간
                .expiration(new Date(now + expiry))// 이 토큰이 언제 소멸될지 설정 (생명주기)
                .signWith(secretKey) // 시크릿키를 통해 시그니처를 만들어 암호화 진행
                .compact(); // 토큰을 컴팩트 하여 리턴, 위에서 설정한 Header + Payload + Signature를 .으로 구분된 문자열로 압축
    }
}