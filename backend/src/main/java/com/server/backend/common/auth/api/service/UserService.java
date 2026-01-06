package com.server.backend.common.auth.api.service;

import com.server.backend.common.auth.dto.CustomOAuth2User;
import com.server.backend.common.auth.dto.UserDTO;
import com.server.backend.common.auth.dto.UserResponseDTO;
import com.server.backend.common.data.entity.UserEntity;
import com.server.backend.common.data.enums.SocialProviderType;
import com.server.backend.common.data.enums.UserRoleType;
import com.server.backend.common.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;



    // 자체 로그인 회원가입 (존재 여부)
    @Transactional(readOnly = true)
    public Boolean existUser(UserDTO userDTO) {
        log.info("## AuthService existUser");

        return userRepository.existsById(userDTO.getUserId());
    }



    // 자체 로그인 회원가입
    @Transactional
    public void signUp(UserDTO userDTO) {
        log.info("## UserService signUp");

        if(userRepository.existsById(userDTO.getUserId())) {
            throw new IllegalArgumentException("존재하는 유저입니다.");
        }

        // TODO 임시 데이터 변경 필요
        userRepository.save(
                UserEntity.builder()
                        .userId(userDTO.getUserId())
                        .userPw(bCryptPasswordEncoder.encode(userDTO.getUserPw()))
                        .userEmail(userDTO.getUserEmail())
                        .userRole(UserRoleType.USER)
                        .nickname(userDTO.getNickname())
                        .isLock(false) // 임시
                        .isSocial(false) // 임시
                        .socialProviderType(SocialProviderType.LOCAL) // 임시
                        .build()
        );
    }



    // 자체 로그인
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("## UserService loadUserByUsername");

        UserEntity userEntity = userRepository.findByUserIdAndIsLockAndIsSocial(username, false, false)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.builder()
                .username(userEntity.getUserId())
                .password(userEntity.getUserPw())
                .roles(userEntity.getUserRole().name()) // .roles 하면 내부적으로 자동 ROLE_ 이 붙는다.
                .accountLocked(userEntity.getIsLock())
                .build();
    }



    // 자체 로그인 회원 정보 수정
    @Transactional
    public void updateUser(UserDTO userDTO) throws AccessDeniedException {
        log.info("## UserService updateUser");

        // 본인만 수정 가능 검증
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!sessionUsername.equals(userDTO.getUserId())) {
            throw new AccessDeniedException("본인 계정만 수정 가능");
        }

        UserEntity userEntity = userRepository.findByUserIdAndIsLockAndIsSocial(userDTO.getUserId(), false, false)
                .orElseThrow(() -> new UsernameNotFoundException(userDTO.getUserId()));

        // 여기서 회원정보 수정이 이루어짐
        userEntity.updateUser(userDTO);
    }



    // 자체/소셜 로그인 회원 탈퇴
    @Transactional
    public void deleteUser(UserDTO userDTO) throws AccessDeniedException {
        log.info("## UserService deleteUser");

        // 본인 및 어드민만 삭제 가능 검증
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        String sessionUsername = contextHolder.getAuthentication().getName();
        String sessionRole = contextHolder.getAuthentication().getAuthorities().iterator().next().toString();

        boolean isOwner = sessionUsername.equals(userDTO.getUserId());
        boolean isAdmin = sessionRole.equals("ROLE_" + UserRoleType.SYS.name());

        if(!isOwner && !isAdmin) {
            throw new AccessDeniedException("본인 혹인 관리자만 삭제할 수 있습니다.");
        }

        // 유저 제거
        userRepository.deleteById(userDTO.getUserId());

        // RefreshToken 제거
        jwtService.removeRefreshUser(userDTO.getUserId());
    }



    // 소셜 로그인 (로그인시: 신규 = 가입, 기존 = 업데이트)
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("## UserService loadUser");

        // 부모 메소드 호출
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 데이터
        Map<String, Object> attributes;
        List<GrantedAuthority> authorities;

        String username;
        String role = UserRoleType.USER.name();
        String email;
        String nickname;

        // Provider 제공자별 데이터 획득
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        if(registrationId.equals(SocialProviderType.NAVER.name())) {

            attributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            username = registrationId + "_" + attributes.get("id");
            email = attributes.get("email").toString();
            nickname = attributes.get("nickname").toString();

        } else if(registrationId.equals(SocialProviderType.GOOGLE.name())) {

            attributes = (Map<String, Object>) oAuth2User.getAttributes();
            username = registrationId + "_" + attributes.get("sub");
            email = attributes.get("email").toString();
            nickname = attributes.get("nickname").toString();

        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        // 데이터베이스 조회 -> 존재하면 업데이트, 없으면 신규 가입
        Optional<UserEntity> userEntity = userRepository.findByUserIdAndIsSocial(username, true);

        if(userEntity.isPresent()) {
            // role 조회
            role = userEntity.get().getUserRole().name();

            // 기존 유저 업데이트
            UserDTO userDTO = new UserDTO();
            userDTO.setNickname(nickname);
            userDTO.setUserEmail(email);
            userEntity.get().updateUser(userDTO);

            userRepository.save(userEntity.get());
        } else {
            // 신규 유저 추가
            UserEntity user = UserEntity.builder()
                    .userId(username)
                    .userPw("")
                    .isLock(false)
                    .isSocial(true)
                    .socialProviderType(SocialProviderType.valueOf(registrationId))
                    .userRole(UserRoleType.USER)
                    .nickname(nickname)
                    .userEmail(email)
                    .build();

            userRepository.save(user);
        }

        authorities = List.of(new SimpleGrantedAuthority(role));

        return new CustomOAuth2User(attributes, authorities, username);
    }



    // 자체/소셜 유저 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDTO readUser() {
        log.info("## UserService readUser");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByUserIdAndIsLock(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다" + username));

        return new UserResponseDTO(username, userEntity.getIsSocial(), userEntity.getNickname(), userEntity.getUserEmail());
    }
}
