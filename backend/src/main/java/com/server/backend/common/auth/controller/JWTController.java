package com.server.backend.common.auth.controller;

import com.server.backend.common.auth.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/jwt")
public class JWTController {

    private final JWTService jwtService;

}
