package com.server.backend.common.data.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.backend.common.data.repository.custom.TravelRepositoryCustom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TravelRepositoryCustomImpl implements TravelRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
