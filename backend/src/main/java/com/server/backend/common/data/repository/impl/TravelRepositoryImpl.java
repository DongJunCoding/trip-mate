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

        Long travelId = dto.getTravelId();

        // 1. 여행 기본 정보
        TravelDTO travelDTO = queryFactory
                .select(Projections.bean(TravelDTO.class,
                        travel.travelId,
                        travel.teamName,
                        travel.destination,
                        travel.startDate,
                        travel.endDate
                ))
                .from(travel)
                .where(travel.travelId.eq(travelId))
                .fetchOne();

        if (travelDTO == null) {
            throw new RuntimeException("해당 여행 정보가 없습니다. travelId=" + travelId);
        }

        // 2. 일차 목록 (distinct dayNum, scheduleDate)
        List<TravelDayDTO> daysDTO = queryFactory
                .select(Projections.bean(TravelDayDTO.class,
                        schedule.dayNum,
                        schedule.scheduleDate
                ))
                .distinct()
                .from(schedule)
                .where(schedule.travelId.eq(travelId))
                .orderBy(schedule.dayNum.asc())
                .fetch();

        // 3. 각 일차별 세부 일정
        for (TravelDayDTO dayDTO : daysDTO) {
            List<TravelScheduleDTO> schedules = queryFactory
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
                    .where(
                            schedule.travelId.eq(travelId),
                            schedule.dayNum.eq(dayDTO.getDayNum())
                    )
                    .orderBy(schedule.visitTime.asc().nullsLast()) // null 처리
                    .fetch();

            dayDTO.setSchedules(schedules);
        }

        travelDTO.setDays(daysDTO);
        return travelDTO;
    }
}