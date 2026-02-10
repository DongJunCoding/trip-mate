package com.server.backend.api.v1.app.service;

import com.server.backend.common.data.dto.TravelDTO;
import com.server.backend.common.data.entity.TravelEntity;
import com.server.backend.common.data.repository.TravelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;

    @Transactional
    public void saveSchedule(TravelDTO dto) {
        log.info("## TravelService saveSchedule");

        TravelEntity travelEntity = TravelEntity.builder()
                .teamName(dto.getTeamName())
                .destination(dto.getDestination())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        travelRepository.save(travelEntity);
    }

    public List<TravelDTO> getTravelList(TravelDTO dto) {
        log.info("## TravelService getTravelList");

        return travelRepository.selectTravelList();
    }
}
