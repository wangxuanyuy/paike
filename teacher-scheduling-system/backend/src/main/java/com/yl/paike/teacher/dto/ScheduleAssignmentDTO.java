package com.yl.paike.teacher.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleAssignmentDTO {
    private Long courseScheduleId;
    private String courseName;
    private String classroomName;
    private LocalDate scheduleDate;
    private String timeSlotName;
    private List<TeacherAssignmentDetailDTO> teacherDetails;
}
