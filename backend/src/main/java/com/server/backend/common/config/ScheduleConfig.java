package com.server.backend.common.config;

import com.server.backend.common.data.repository.UserTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduleConfig {

    private final UserTokenRepository userTokenRepository;

    public ScheduleConfig(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    // Refresh 토큰 저장소 8일 지난 토큰 삭제
    @Scheduled(cron = "0 0 3 * * *")
    public void refreshEntityTtlSchedule() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(8);
        userTokenRepository.deleteByAddedDateBefore(cutoff);
    }
}