package com.server.backend.api.v1.app.controller;

import com.server.backend.api.v1.app.service.TravelService;
import com.server.backend.common.data.dto.TravelDTO;
import com.server.backend.common.data.dto.TravelScheduleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/travel")
public class TravelController {

    private final TravelService travelService;

    // 여행일정 저장
    @PostMapping("/saveSchedule")
    public ResponseEntity<?> saveSchedule(@RequestBody TravelDTO dto) {
        log.info("## TravelController saveSchedule");

        travelService.saveSchedule(dto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 여행일정 리스트
    @PostMapping("/getTravelList")
    public ResponseEntity<?> getTravelList(@RequestBody TravelDTO dto) {
        log.info("## TravelController getTravelList");

        return ResponseEntity.status(HttpStatus.OK).body(travelService.getTravelList(dto));
    }

    @GetMapping("/user")
    public void user() {
        log.info("## TravelController ");
    }
}
