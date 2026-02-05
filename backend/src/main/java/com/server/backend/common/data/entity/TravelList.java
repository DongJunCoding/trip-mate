package com.server.backend.common.data.entity;


import com.server.backend.common.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "travel_list")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelList extends BaseEntity {

  private String teamName;
  private String destination;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
