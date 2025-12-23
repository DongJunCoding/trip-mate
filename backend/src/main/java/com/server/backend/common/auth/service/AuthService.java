package com.server.backend.common.auth.service;

import com.server.backend.common.auth.dto.UserDTO;
import com.server.backend.common.data.entity.UserEntity;
import com.server.backend.common.data.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUp(UserDTO userDTO) {
        log.info("### signUp Service");

        userRepository.save(
                UserEntity.builder()
                        .userId(userDTO.getUserId())
                        .userPw(bCryptPasswordEncoder.encode(userDTO.getUserPw()))
                        .userEmail(userDTO.getUserEmail())
                        .userRole(userDTO.getUserRole())
                        .nickname(userDTO.getNickname())
                        .build()
        );

    }
}
