package com.server.backend.common.data.repository;

import com.server.backend.common.data.entity.TravelEntity;
import com.server.backend.common.data.entity.TravelGroupEntityPK;
import com.server.backend.common.data.repository.custom.TravelRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<TravelEntity, TravelGroupEntityPK>, TravelRepositoryCustom {
}
