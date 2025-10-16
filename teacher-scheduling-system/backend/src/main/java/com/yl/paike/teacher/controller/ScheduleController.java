package com.yl.paike.teacher.controller;

import com.yl.paike.teacher.dto.*;
import com.yl.paike.teacher.service.ScheduleService;
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
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    @PostMapping
    public Result<CourseScheduleDTO> createSchedule(
            @Valid @RequestBody CourseScheduleCreateDTO createDTO) {
        log.info("创建课程安排: 课程ID={}, 日期={}", 
                createDTO.getCourseId(), createDTO.getScheduleDate());
        CourseScheduleDTO schedule = scheduleService.createSchedule(createDTO);
        return Result.success(schedule, "课程安排创建成功");
    }
    
    @PostMapping("/assign")
    public Result<ScheduleAssignmentDTO> assignTeachers(
            @Valid @RequestBody ScheduleAssignmentRequestDTO requestDTO) {
        log.info("分配教师: 课程安排ID={}, 教师数量={}", 
                requestDTO.getCourseScheduleId(), requestDTO.getTeacherIds().size());
        ScheduleAssignmentDTO assignment = scheduleService.assignTeachers(requestDTO);
        return Result.success(assignment, "排课成功");
    }
    
    @GetMapping
    public Result<PageResult<CourseScheduleDTO>> getScheduleList(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "scheduleDate"));
        Page<CourseScheduleDTO> schedulePage = scheduleService.getScheduleList(
            startDate, endDate, status, pageable);
        
        PageResult<CourseScheduleDTO> pageResult = new PageResult<>();
        pageResult.setContent(schedulePage.getContent());
        pageResult.setTotalElements(schedulePage.getTotalElements());
        pageResult.setTotalPages(schedulePage.getTotalPages());
        pageResult.setPage(page);
        pageResult.setSize(size);
        
        return Result.success(pageResult);
    }
    
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelSchedule(
            @PathVariable Long id,
            @RequestParam String reason) {
        log.info("取消课程安排: ID={}, 原因={}", id, reason);
        scheduleService.cancelSchedule(id, reason);
        return Result.success(null, "课程取消成功");
    }
    
    @GetMapping("/teacher/{teacherId}")
    public Result<List<CourseScheduleDTO>> getTeacherSchedules(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        List<CourseScheduleDTO> schedules = scheduleService.getTeacherSchedules(
            teacherId, startDate, endDate);
        return Result.success(schedules);
    }
}
