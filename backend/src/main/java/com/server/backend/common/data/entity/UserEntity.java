package com.server.backend.common.data.entity;

import com.server.backend.common.base.entity.BaseEntity;
import com.server.backend.common.data.enums.SocialProviderType;
import com.server.backend.common.data.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    private String userId; // 아이디
    private String userPw; // 패스워드
    private String userEmail; // 이메일

    @Enumerated(EnumType.STRING)
    private UserRoleType userRole; // 권한

    private String nickname; // 닉네임
    private Boolean isLock; // 계정 Lock 구분
    private Boolean isSocial; // 소셜 계정 구분

    @Enumerated(EnumType.STRING)
    private SocialProviderType socialProviderType; // 소셜 계정 타입

}
