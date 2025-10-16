package com.yl.paike.teacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class TeacherCreateDTO {
    
    @NotBlank(message = "教师姓名不能为空")
    @Size(min = 2, max = 50, message = "教师姓名长度应在2-50个字符")
    private String teacherName;
    
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String specialties;
    
    @NotEmpty(message = "可教年龄组不能为空")
    private List<Integer> ageGroups;
}
