package com.yl.paike.teacher.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseDTO {
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer ageGroup;
    private String ageGroupName;
    private Integer maxStudents;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
