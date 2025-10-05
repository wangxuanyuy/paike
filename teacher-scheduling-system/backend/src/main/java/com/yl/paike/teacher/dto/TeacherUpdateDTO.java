package com.yl.paike.teacher.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class TeacherUpdateDTO {
    
    @Size(min = 2, max = 50, message = "教师姓名长度应在2-50个字符")
    private String teacherName;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String specialties;
    
    private List<Integer> ageGroups;
}
