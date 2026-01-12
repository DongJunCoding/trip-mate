package com.server.backend.common.config;

import com.server.backend.common.auth.api.service.JWTService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleConfig {

    private final JWTService jwtService;

    public ScheduleConfig(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    // Refresh 토큰 저장소 8일 지난 토큰 삭제
    @Scheduled(cron = "0 0 3 * * *")
    public void refreshEntityTtlSchedule() {
        jwtService.refreshEntityTtlSchedule();
    }
}