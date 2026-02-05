package com.server.backend.common.data.repository;

import com.server.backend.common.data.entity.TravelEntity;
import com.server.backend.common.data.entity.TravelGroupEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<TravelEntity, TravelGroupEntityPK> {

}
