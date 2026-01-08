package com.server.backend.common.auth.api.controller;

import com.server.backend.common.auth.dto.JWTTokenDTO;
import com.server.backend.common.auth.dto.UserDTO;
import com.server.backend.common.auth.api.service.UserService;
import com.server.backend.common.auth.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/user")
public class UserController {

    private final UserService userService;

    // 자체 로그인 유저 존재 확인
    @PostMapping(value = "/exist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existUser(@Validated(UserDTO.existGroup.class) @RequestBody UserDTO userDTO) {
        log.info("## UserController existUser");

        return ResponseEntity.ok(userService.existUser(userDTO));
    }

    // 회원가입
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@Validated(UserDTO.addGroup.class) @RequestBody UserDTO userDTO) {
        log.info("## UserController signUp");

        userService.signUp(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 유저 정보
    @GetMapping(value = "/userInfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDTO readUser() {
        log.info("## UserController readUser");

        return userService.readUser();
    }

    // 유저 수정 (자체 로그인 유저만)
    @PutMapping(value = "/updateUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@Validated(UserDTO.updateGroup.class) @RequestBody UserDTO userDTO) throws AccessDeniedException {
        log.info("## UserController updateUser");

        userService.updateUser(userDTO);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    // 유저 제거 (자체/소셜)
    @DeleteMapping(value = "/deleteUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@Validated(UserDTO.deleteGroup.class) @RequestBody UserDTO userDTO) throws AccessDeniedException {
        log.info("## UserController deleteUser");

        userService.deleteUser(userDTO);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
