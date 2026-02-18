package com.server.backend.common.data.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.backend.common.data.dto.TravelDTO;
import com.server.backend.common.data.entity.QTravelEntity;
import com.server.backend.common.data.entity.QTravelScheduleEntity;
import com.server.backend.common.data.repository.custom.TravelRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TravelRepositoryImpl implements TravelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TravelDTO> selectTravelList() {
        QTravelEntity travel = QTravelEntity.travelEntity;

        return queryFactory
                .select(
                        Projections.bean(TravelDTO.class,
                                travel.travelId,
                                travel.teamName,
                                travel.destination,
                                travel.startDate,
                                travel.endDate
                        ))
                .from(travel)
                .fetch();
    }

    @Override
    public TravelDTO selectSchedule(TravelDTO dto) {

        QTravelScheduleEntity schedule = QTravelScheduleEntity.travelScheduleEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if(dto.getTravelId() != null) {
            builder.and(schedule.travelId.eq(dto.getTravelId()));
        }

        return null;
    }
}