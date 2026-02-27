package com.server.backend.api.v1.app.service;

import com.server.backend.common.data.dto.TravelDTO;
import com.server.backend.common.data.dto.TravelDayDTO;
import com.server.backend.common.data.dto.TravelScheduleDTO;
import com.server.backend.common.data.entity.TravelEntity;
import com.server.backend.common.data.entity.TravelScheduleEntity;
import com.server.backend.common.data.repository.TravelRepository;
import com.server.backend.common.data.repository.TravelScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final TravelScheduleRepository travelScheduleRepository;

    @Transactional
    public void saveTravel(TravelDTO dto) {
        log.info("## TravelService saveTravel");

        TravelEntity travelEntity;

        if (dto.getTravelId() == null) {
            // 신규
            travelEntity = TravelEntity.builder()
                    .teamName(dto.getTeamName())
                    .destination(dto.getDestination())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .build();
            travelRepository.save(travelEntity);
        } else {
            // 수정
            travelEntity = travelRepository.findById(dto.getTravelId())
                    .orElseThrow(() -> new RuntimeException("Travel not found"));
            travelEntity.setTeamName(dto.getTeamName());
            travelEntity.setDestination(dto.getDestination());
            travelEntity.setStartDate(dto.getStartDate());
            travelEntity.setEndDate(dto.getEndDate());

            // dto에 없는 스케줄 삭제
            List<Long> dtoScheduleIds = dto.getDays().stream()
                    .flatMap(day -> day.getSchedules().stream())
                    .map(TravelScheduleDTO::getScheduleId)
                    .filter(id -> id != null)
                    .toList();

            List<TravelScheduleEntity> existingSchedules = travelScheduleRepository.findByTravelId(travelEntity.getTravelId());
            List<TravelScheduleEntity> toDelete = existingSchedules.stream()
                    .filter(s -> !dtoScheduleIds.contains(s.getScheduleId()))
                    .toList();

            travelScheduleRepository.deleteAll(toDelete);
        }

        // 스케줄 저장
        List<TravelScheduleEntity> travelScheduleEntityList = new ArrayList<>();

        for (TravelDayDTO day : dto.getDays()) {
            for (TravelScheduleDTO schedule : day.getSchedules()) {

                TravelScheduleEntity scheduleEntity;

                if (schedule.getScheduleId() == null) {
                    // 신규 스케줄
                    scheduleEntity = TravelScheduleEntity.builder()
                            .travelId(travelEntity.getTravelId())
                            .dayNum(day.getDayNum())
                            .scheduleDate(day.getScheduleDate())
                            .place(schedule.getPlace())
                            .address(schedule.getAddress())
                            .lat(schedule.getLat())
                            .lng(schedule.getLng())
                            .visitTime(schedule.getVisitTime())
                            .memo(schedule.getMemo())
                            .build();
                } else {
                    // 기존 스케줄 수정
                    scheduleEntity = travelScheduleRepository.findById(schedule.getScheduleId())
                            .orElseThrow(() -> new RuntimeException("Schedule not found"));
                    scheduleEntity.setDayNum(day.getDayNum());
                    scheduleEntity.setScheduleDate(day.getScheduleDate());
                    scheduleEntity.setPlace(schedule.getPlace());
                    scheduleEntity.setAddress(schedule.getAddress());
                    scheduleEntity.setLat(schedule.getLat());
                    scheduleEntity.setLng(schedule.getLng());
                    scheduleEntity.setVisitTime(schedule.getVisitTime());
                    scheduleEntity.setMemo(schedule.getMemo());
                }

                travelScheduleEntityList.add(scheduleEntity);
            }
        }

        travelScheduleRepository.saveAll(travelScheduleEntityList);
    }

    public List<TravelDTO> getTravelList(TravelDTO dto, String userId) {
        log.info("## TravelService getTravelList");

        return travelRepository.getTravelList(dto, userId);
    }

    public TravelDTO getTravelSchedule(TravelDTO dto, String userId) {
        log.info("## TravelService getTravelSchedule");

        return travelRepository.getTravelSchedule(dto, userId);
    }
}
