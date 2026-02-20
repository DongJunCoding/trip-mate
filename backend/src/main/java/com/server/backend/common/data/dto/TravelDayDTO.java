package com.server.backend.common.data.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TravelDayDTO {

    private Long dayNum;
    private LocalDate scheduleDate;

    private List<TravelScheduleDTO> schedules;
}