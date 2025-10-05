package com.yl.paike.teacher.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassroomDTO {
    private Long id;
    private String classroomCode;
    private String classroomName;
    private Integer maxCapacity;
    private String location;
    private String facilities;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
