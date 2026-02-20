package com.server.backend.api.v1.app.controller;

import com.server.backend.api.v1.app.service.TravelService;
import com.server.backend.common.auth.security.util.SecurityUtil;
import com.server.backend.common.data.dto.TravelDTO;
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
    @PostMapping("/saveTravel")
    public ResponseEntity<?> saveTravel(@RequestBody TravelDTO dto) {
        log.info("## TravelController saveTravel");

        travelService.saveTravel(dto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 여행일정 리스트
    @PostMapping("/getTravelList")
    public ResponseEntity<?> getTravelList(@RequestBody TravelDTO dto) {
        log.info("## TravelController getTravelList");

        String userId = SecurityUtil.getUserId();

        return ResponseEntity.status(HttpStatus.OK).body(travelService.getTravelList(dto, userId));
    }

    // 일정 상세
    @PostMapping("/getTravelSchedule")
    public ResponseEntity<?> getTravelSchedule(@RequestBody TravelDTO dto) {
        log.info("## TravelController getTravelSchedule");

        String userId = SecurityUtil.getUserId();

        return ResponseEntity.status(HttpStatus.OK).body(travelService.getTravelSchedule(dto, userId));
    }

}
