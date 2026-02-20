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

        TravelEntity travelEntity = TravelEntity.builder()
                .teamName(dto.getTeamName())
                .destination(dto.getDestination())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        travelRepository.save(travelEntity);

        List<TravelScheduleEntity> travelScheduleEntityList = new ArrayList<>();
        List<TravelDayDTO> days = dto.getDays();

        for(TravelDayDTO day: days) {
            List<TravelScheduleDTO> schedules = day.getSchedules();

            for(TravelScheduleDTO schedule : schedules) {
                TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                        .travelId(travelEntity.getTravelId())
                        .dayNum(day.getDayNum())
                        .scheduleDate(day.getScheduleDate())
                        .place(schedule.getPlace())
                        .address(schedule.getAddress())
                        .lat("")
                        .lng("")
                        .visitTime(schedule.getVisitTime())
                        .memo(schedule.getMemo())
                        .build();

                travelScheduleEntityList.add(travelScheduleEntity);
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
