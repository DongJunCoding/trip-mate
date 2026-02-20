package com.server.backend.common.data.repository.custom;

import com.server.backend.common.data.dto.TravelDTO;

import java.util.List;

public interface TravelRepositoryCustom {

    List<TravelDTO> getTravelList(TravelDTO dto, String userId);

    TravelDTO getTravelSchedule(TravelDTO id, String userId);
}
