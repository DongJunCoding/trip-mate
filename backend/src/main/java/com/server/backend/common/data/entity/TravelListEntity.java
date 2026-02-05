package com.server.backend.common.data.entity;


import com.server.backend.common.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "travel_list")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelListEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String teamName;
  private String destination;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
