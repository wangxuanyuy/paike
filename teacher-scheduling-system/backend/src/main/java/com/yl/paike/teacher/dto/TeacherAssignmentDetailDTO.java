package com.yl.paike.teacher.dto;

import lombok.Data;

@Data
public class TeacherAssignmentDetailDTO {
    private Long assignmentId;
    private Long teacherId;
    private String teacherName;
    private Boolean isMainTeacher;
}
