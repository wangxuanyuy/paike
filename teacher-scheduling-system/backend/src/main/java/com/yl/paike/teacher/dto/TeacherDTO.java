package com.yl.paike.teacher.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeacherDTO {
    private Long id;
    private String teacherCode;
    private String teacherName;
    private String phone;
    private String email;
    private String specialties;
    private List<Integer> ageGroups;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
