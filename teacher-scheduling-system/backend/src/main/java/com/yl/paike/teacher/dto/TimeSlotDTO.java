package com.yl.paike.teacher.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotDTO {
    private Long id;
    private String slotName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer dayOfWeek;
    private String dayOfWeekName;
    private Boolean isActive;
}
