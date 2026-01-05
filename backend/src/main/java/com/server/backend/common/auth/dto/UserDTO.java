package com.server.backend.common.auth.dto;

import com.server.backend.common.data.enums.SocialProviderType;
import com.server.backend.common.data.enums.UserRoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {

    public interface existGroup {} // 회원 가입시 username 존재 확인
    public interface addGroup {} // 회원가입시
    public interface passwordGroup {} // 비밀번호 변경시
    public interface updateGroup {} // 회원 수정시
    public interface deleteGroup {} // 회원 삭제시

    @NotBlank(groups = {existGroup.class, addGroup.class, updateGroup.class, deleteGroup.class}) @Size(min = 4)
    private String userId; // 로그인 아이디

    @NotBlank(groups = {addGroup.class, passwordGroup.class}) @Size(min = 4)
    private String userPw; // 로그인 비밀번호

    @NotBlank(groups = {addGroup.class, updateGroup.class})
    private String nickname; // 닉네임

    @Email(groups = {addGroup.class, updateGroup.class})
    private String userEmail; // 이메일

    private UserRoleType userRole; // 권한
    private SocialProviderType socialProviderType; // 소셜 계정 타입
}
