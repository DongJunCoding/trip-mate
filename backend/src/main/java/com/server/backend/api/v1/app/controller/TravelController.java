package com.server.backend.api.v1.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/travel")
public class TravelController {
    
    @GetMapping("/saveSchedule")
    public ResponseEntity<?> saveSchedule() {
        log.info("## TravelController saveSchedule");

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user")
    public void user() {
        log.info("## TravelController ");
    }
}
