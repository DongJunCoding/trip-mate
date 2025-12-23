package com.server.backend.common.auth.dto;

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
    private String userRole; // 권한
    private String nickname; // 닉네임
}
