package com.server.backend.common.data.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.backend.common.data.dto.TravelDTO;
import com.server.backend.common.data.dto.TravelDayDTO;
import com.server.backend.common.data.dto.TravelScheduleDTO;
import com.server.backend.common.data.entity.QTravelEntity;
import com.server.backend.common.data.entity.QTravelGroupEntity;
import com.server.backend.common.data.entity.QTravelScheduleEntity;
import com.server.backend.common.data.repository.custom.TravelRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TravelRepositoryImpl implements TravelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TravelDTO> getTravelList(TravelDTO dto, String userId) {
        QTravelEntity travel = QTravelEntity.travelEntity;
        QTravelGroupEntity group = QTravelGroupEntity.travelGroupEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if(userId != null && !userId.isBlank()) {
            builder.and(group.userId.eq(userId));
        }

        return queryFactory
                .select(
                        Projections.bean(TravelDTO.class,
                                travel.travelId,
                                travel.teamName,
                                travel.destination,
                                travel.startDate,
                                travel.endDate
                        ))
                .from(group)
                .leftJoin(travel)
                .on(group.travelId.eq(travel.travelId))
                .where(builder)
                .fetch();
    }

    @Override
    public TravelDTO getTravelSchedule(TravelDTO dto, String userId) {

        QTravelEntity travel = QTravelEntity.travelEntity;
        QTravelScheduleEntity schedule = QTravelScheduleEntity.travelScheduleEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if(dto.getTravelId() != null) {
            builder.and(schedule.travelId.eq(dto.getTravelId()));
        }

        TravelDTO travelDTO = queryFactory
                .select(Projections.bean(TravelDTO.class,
                        travel.travelId,
                        travel.teamName,
                        travel.destination,
                        travel.startDate,
                        travel.endDate
                ))
                .from(travel)
                .where(travel.travelId.eq(dto.getTravelId()))
                .fetchOne();


        List<TravelDayDTO> daysDTO = queryFactory
                .select(Projections.bean(TravelDayDTO.class,
                        schedule.dayNum,
                        schedule.scheduleDate
                        ))
                .distinct()
                .from(schedule)
                .where(builder)
                .fetch();


        for(TravelDayDTO dayDTO : daysDTO) {

            List<TravelScheduleDTO> scheduleDTO = queryFactory
                    .select(Projections.bean(TravelScheduleDTO.class,
                            schedule.scheduleId,
                            schedule.place,
                            schedule.address,
                            schedule.lat,
                            schedule.lng,
                            schedule.visitTime,
                            schedule.memo
                    ))
                    .from(schedule)
                    .where(builder, schedule.dayNum.eq(dayDTO.getDayNum()))
                    .orderBy(schedule.dayNum.asc(), schedule.visitTime.asc())
                    .fetch();

            dayDTO.setSchedules(scheduleDTO);
        }



        assert travelDTO != null;
        travelDTO.setDays(daysDTO);



        return travelDTO;
    }
}