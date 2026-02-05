package com.server.backend.common.data.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TravelDTO {
    private Long id;
    private String teamName;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
}
