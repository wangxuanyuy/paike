package com.yl.paike.teacher.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleAssignmentRequestDTO {
    
    @NotNull(message = "课程安排ID不能为空")
    private Long courseScheduleId;
    
    @NotEmpty(message = "教师列表不能为空")
    @Size(min = 1, max = 3, message = "每堂课需要1-3名教师")
    private List<Long> teacherIds;
    
    @NotNull(message = "主讲教师不能为空")
    private Long mainTeacherId;
}
