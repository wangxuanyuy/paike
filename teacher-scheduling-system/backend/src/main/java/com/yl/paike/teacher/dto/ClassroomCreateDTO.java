package com.yl.paike.teacher.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClassroomCreateDTO {
    
    @NotBlank(message = "教室名称不能为空")
    @Size(min = 2, max = 100, message = "教室名称长度应在2-100个字符")
    private String classroomName;
    
    @NotNull(message = "最大容量不能为空")
    @Min(value = 1, message = "最大容量至少为1")
    @Max(value = 100, message = "最大容量不能超过100")
    private Integer maxCapacity;
    
    private String location;
    
    private String facilities;
}
