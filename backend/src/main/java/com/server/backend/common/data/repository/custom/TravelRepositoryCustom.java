package com.server.backend.common.data.repository.custom;

import com.server.backend.common.data.dto.TravelDTO;

import java.util.List;

public interface TravelRepositoryCustom {

    List<TravelDTO> selectTravelList();
}
