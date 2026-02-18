package com.server.backend.common.data.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TravelScheduleDTO {

    private Long scheduleId;
    private String place;
    private String address;
    private String lat;
    private String lng;
    private LocalTime visitTime;
    private String memo;

}
