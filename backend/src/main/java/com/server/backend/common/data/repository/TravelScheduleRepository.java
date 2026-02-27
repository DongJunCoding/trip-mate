package com.server.backend.common.data.repository;

import com.server.backend.common.data.entity.TravelScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelScheduleRepository extends JpaRepository<TravelScheduleEntity, Long> {
    List<TravelScheduleEntity> findByTravelId(Long travelId);
}
