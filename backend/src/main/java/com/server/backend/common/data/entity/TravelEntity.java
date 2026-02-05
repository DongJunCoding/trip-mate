package com.server.backend.common.data.entity;


import com.server.backend.common.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "travel")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String teamName;
  private String destination;
  private LocalDate startDate;
  private LocalDate endDate;
}
