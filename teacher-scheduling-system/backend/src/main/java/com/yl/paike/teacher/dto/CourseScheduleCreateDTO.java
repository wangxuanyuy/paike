package com.yl.paike.teacher.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseScheduleCreateDTO {
    
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
    
    @NotNull(message = "时间段ID不能为空")
    private Long timeSlotId;
    
    @NotNull(message = "教室ID不能为空")
    private Long classroomId;
    
    @NotNull(message = "上课日期不能为空")
    private LocalDate scheduleDate;
}
