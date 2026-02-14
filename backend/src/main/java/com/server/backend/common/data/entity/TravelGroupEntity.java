package com.server.backend.common.data.entity;

import com.server.backend.common.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "travel_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TravelGroupEntityPK.class)
public class TravelGroupEntity extends BaseEntity {

  @Id
  private Long travelId;

  @Id
  private String userId;

  private String teamRole;

}
