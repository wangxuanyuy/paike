package com.yl.paike.teacher.service;

import com.yl.paike.teacher.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    
    CourseScheduleDTO createSchedule(CourseScheduleCreateDTO createDTO);
    
    ScheduleAssignmentDTO assignTeachers(ScheduleAssignmentRequestDTO requestDTO);
    
    Page<CourseScheduleDTO> getScheduleList(LocalDate startDate, LocalDate endDate, Integer status, Pageable pageable);
    
    void cancelSchedule(Long scheduleId, String reason);
    
    List<CourseScheduleDTO> getTeacherSchedules(Long teacherId, LocalDate startDate, LocalDate endDate);
}
