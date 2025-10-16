package com.yl.paike.teacher.service;

import com.yl.paike.teacher.dto.CourseCreateDTO;
import com.yl.paike.teacher.dto.CourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    
    CourseDTO createCourse(CourseCreateDTO createDTO);
    
    CourseDTO getCourseById(Long id);
    
    Page<CourseDTO> getCourseList(Integer ageGroup, String keyword, Pageable pageable);
    
    CourseDTO updateCourse(Long id, CourseCreateDTO updateDTO);
    
    void deleteCourse(Long id);
    
    List<CourseDTO> getAllActiveCourses();
}
