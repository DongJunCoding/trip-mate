package com.server.backend.common.data.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TravelDTO {
    private Long travelId;
    private String userId;
    private String teamName;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;

    private List<TravelDayDTO> days;
}
