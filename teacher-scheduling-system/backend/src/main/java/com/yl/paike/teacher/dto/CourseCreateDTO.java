package com.yl.paike.teacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseCreateDTO {
    
    @NotBlank(message = "课程名称不能为空")
    @Size(min = 2, max = 100, message = "课程名称长度应在2-100个字符")
    private String courseName;
    
    @NotNull(message = "年龄组不能为空")
    @Min(value = 1, message = "年龄组值应在1-4之间")
    @Max(value = 4, message = "年龄组值应在1-4之间")
    private Integer ageGroup;
    
    @NotNull(message = "最大学生数不能为空")
    @Min(value = 1, message = "最大学生数至少为1")
    @Max(value = 50, message = "最大学生数不能超过50")
    private Integer maxStudents;
    
    private String description;
}
