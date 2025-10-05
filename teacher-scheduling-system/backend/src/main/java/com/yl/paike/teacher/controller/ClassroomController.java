package com.yl.paike.teacher.controller;

import com.yl.paike.teacher.dto.ClassroomCreateDTO;
import com.yl.paike.teacher.dto.ClassroomDTO;
import com.yl.paike.teacher.dto.Result;
import com.yl.paike.teacher.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
@Slf4j
public class ClassroomController {
    
    private final ClassroomService classroomService;
    
    @PostMapping
    public Result<ClassroomDTO> createClassroom(@Valid @RequestBody ClassroomCreateDTO createDTO) {
        log.info("创建教室: {}", createDTO.getClassroomName());
        ClassroomDTO classroom = classroomService.createClassroom(createDTO);
        return Result.success(classroom, "教室创建成功");
    }
    
    @GetMapping("/{id}")
    public Result<ClassroomDTO> getClassroom(@PathVariable Long id) {
        ClassroomDTO classroom = classroomService.getClassroomById(id);
        return Result.success(classroom);
    }
    
    @GetMapping
    public Result<List<ClassroomDTO>> getAllClassrooms() {
        List<ClassroomDTO> classrooms = classroomService.getAllActiveClassrooms();
        return Result.success(classrooms);
    }
    
    @PutMapping("/{id}")
    public Result<ClassroomDTO> updateClassroom(
            @PathVariable Long id,
            @Valid @RequestBody ClassroomCreateDTO updateDTO) {
        log.info("更新教室: {}", id);
        ClassroomDTO classroom = classroomService.updateClassroom(id, updateDTO);
        return Result.success(classroom, "更新成功");
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteClassroom(@PathVariable Long id) {
        log.info("删除教室: {}", id);
        classroomService.deleteClassroom(id);
        return Result.success(null, "删除成功");
    }
}
