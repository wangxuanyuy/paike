package com.yl.paike.teacher.service;

import com.yl.paike.teacher.dto.TeacherCreateDTO;
import com.yl.paike.teacher.dto.TeacherDTO;
import com.yl.paike.teacher.dto.TeacherUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TeacherService {
    
    TeacherDTO createTeacher(TeacherCreateDTO createDTO);
    
    TeacherDTO getTeacherById(Long id);
    
    Page<TeacherDTO> getTeacherList(String keyword, List<Integer> ageGroups, Boolean isActive, Pageable pageable);
    
    TeacherDTO updateTeacher(Long id, TeacherUpdateDTO updateDTO);
    
    void deleteTeacher(Long id);
    
    List<TeacherDTO> getAvailableTeachers(Integer ageGroup, LocalDate date, Long timeSlotId);
}
