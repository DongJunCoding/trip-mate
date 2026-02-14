package com.server.backend.common.data.entity;

import com.server.backend.common.base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "travel_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelScheduleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long travelId;
    private Long dayNum;
    private LocalDate scheduleDate;
    private String place;
    private String address;
    private String lat;
    private String lng;
    private LocalTime visitTime;
    private String memo;

}