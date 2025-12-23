package com.server.backend.api.v1.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/test")
public class TestController {
    
    @GetMapping("/sys")
    public void sys() {
        log.info("## sys Controller");
        log.info("sys 관리자");
    }

    @GetMapping("/user")
    public void user() {
        log.info("## User Controller");
        log.info("user 유저");

        System.out.println("commit test");
    }
}
