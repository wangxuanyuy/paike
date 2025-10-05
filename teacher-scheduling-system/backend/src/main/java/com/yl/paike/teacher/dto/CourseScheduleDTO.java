package com.yl.paike.teacher.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CourseScheduleDTO {
    private Long id;
    private Long courseId;
    private String courseName;
    private Integer ageGroup;
    private Long timeSlotId;
    private String timeSlotName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long classroomId;
    private String classroomName;
    private LocalDate scheduleDate;
    private Integer status;
    private List<String> teacherNames;
    private Integer currentStudents;
    private Integer maxStudents;
}
