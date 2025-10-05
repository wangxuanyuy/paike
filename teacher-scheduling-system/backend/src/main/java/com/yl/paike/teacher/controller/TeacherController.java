package com.yl.paike.teacher.controller;

import com.yl.paike.teacher.dto.*;
import com.yl.paike.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {
    
    private final TeacherService teacherService;
    
    @PostMapping
    public Result<TeacherDTO> createTeacher(@Valid @RequestBody TeacherCreateDTO createDTO) {
        log.info("创建教师: {}", createDTO.getTeacherName());
        TeacherDTO teacher = teacherService.createTeacher(createDTO);
        return Result.success(teacher, "教师创建成功");
    }
    
    @GetMapping("/{id}")
    public Result<TeacherDTO> getTeacher(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.getTeacherById(id);
        return Result.success(teacher);
    }
    
    @GetMapping
    public Result<PageResult<TeacherDTO>> getTeacherList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<Integer> ageGroups,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<TeacherDTO> teacherPage = teacherService.getTeacherList(
            keyword, ageGroups, isActive, pageable);
        
        PageResult<TeacherDTO> pageResult = new PageResult<>();
        pageResult.setContent(teacherPage.getContent());
        pageResult.setTotalElements(teacherPage.getTotalElements());
        pageResult.setTotalPages(teacherPage.getTotalPages());
        pageResult.setPage(page);
        pageResult.setSize(size);
        
        return Result.success(pageResult);
    }
    
    @PutMapping("/{id}")
    public Result<TeacherDTO> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherUpdateDTO updateDTO) {
        log.info("更新教师信息: {}", id);
        TeacherDTO teacher = teacherService.updateTeacher(id, updateDTO);
        return Result.success(teacher, "更新成功");
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteTeacher(@PathVariable Long id) {
        log.info("删除教师: {}", id);
        teacherService.deleteTeacher(id);
        return Result.success(null, "删除成功");
    }
    
    @GetMapping("/available")
    public Result<List<TeacherDTO>> getAvailableTeachers(
            @RequestParam Integer ageGroup,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam Long timeSlotId) {
        
        List<TeacherDTO> teachers = teacherService.getAvailableTeachers(
            ageGroup, date, timeSlotId);
        return Result.success(teachers);
    }
}
