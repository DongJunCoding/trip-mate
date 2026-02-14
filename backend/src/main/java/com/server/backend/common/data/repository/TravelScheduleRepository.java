package com.server.backend.common.data.repository;

import com.server.backend.common.data.entity.TravelScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelScheduleRepository extends JpaRepository<TravelScheduleEntity, Long> {
}
