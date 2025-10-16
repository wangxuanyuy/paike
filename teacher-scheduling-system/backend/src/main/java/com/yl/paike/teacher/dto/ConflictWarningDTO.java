package com.yl.paike.teacher.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConflictWarningDTO {
    private Long id;
    private Integer conflictType;
    private String conflictTypeName;
    private String conflictDescription;
    private List<Long> relatedIds;
    private LocalDate conflictDate;
    private Long timeSlotId;
    private String timeSlotName;
    private Integer status;
    private String statusName;
    private LocalDateTime createdAt;
}
