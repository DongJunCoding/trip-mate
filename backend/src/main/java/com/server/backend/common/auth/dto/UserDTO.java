package com.server.backend.common.auth.dto;

import com.server.backend.common.data.enums.SocialProviderType;
import com.server.backend.common.data.enums.UserRoleType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {

    private String userId; // 로그인 아이디
    private String userPw; // 로그인 비밀번호
    private String userEmail; // 이메일
    private UserRoleType userRole; // 권한
    private String nickname; // 닉네임
    private Boolean isLock; // 계정 Lock 구분
    private Boolean isSocial; // 소셜 계정 구분
    private SocialProviderType socialProviderType; // 소셜 계정 타입
}
