package com.server.backend.common.auth.service;

import com.server.backend.common.auth.dto.UserDTO;
import com.server.backend.common.auth.jwt.util.JWTUtil;
import com.server.backend.common.data.entity.UserEntity;
import com.server.backend.common.data.enums.SocialProviderType;
import com.server.backend.common.data.enums.UserRoleType;
import com.server.backend.common.data.repository.UserRepository;
import com.server.backend.common.data.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 자체 로그인 회원가입 (존재 여부)
    @Transactional(readOnly = true)
    public Boolean existUser(UserDTO userDTO) {
        log.info("## AuthService existUser");

        return userRepository.existsById(userDTO.getUserId());
    }
    
    // 자체 로그인 회원가입
    @Transactional
    public ResponseEntity<?> signUp(UserDTO userDTO) {
        log.info("## UserService signUp");

        if(userRepository.existsById(userDTO.getUserId())) {
            throw new IllegalArgumentException("존재하는 유저입니다.");
        }

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

        return ResponseEntity.ok("Success Signup");
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
                .roles(userEntity.getUserRole().name())
                .accountLocked(userEntity.getIsLock())
                .build();
    }

    // 자체 로그인 회원 정보 수정
    @Transactional
    public ResponseEntity<?> updateUser(UserDTO userDTO) throws AccessDeniedException {
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

        return ResponseEntity.ok().build();
    }
}
