package com.server.backend.common.auth.controller;

import com.server.backend.common.auth.dto.UserDTO;
import com.server.backend.common.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO userDTO) {
        log.info("## Auth Sign Up Controller");

        authService.signUp(userDTO);

        return null;
    }

//    @PostMapping(value = "/login")
//    public ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpServletRequest req) {
//        log.info("## Auth Login Controller");
//
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                userDTO.getUserId(),
//                userDTO.getUserPw()
//        );
//
//        Authentication authentication = authenticationManager.authenticate(token);
//
//
//        // 1️⃣ SecurityContext 생성
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authentication);
//
//        // 2️⃣ ThreadLocal에 저장 (현재 요청)
//        SecurityContextHolder.setContext(context);
//
//        // 3️⃣ 세션 생성 + SecurityContext 저장 (다음 요청용)
//        HttpSession session = req.getSession(true);
//        session.setAttribute(
//                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
//                context
//        );
//        return ResponseEntity.ok().build();
//
//    }
}
