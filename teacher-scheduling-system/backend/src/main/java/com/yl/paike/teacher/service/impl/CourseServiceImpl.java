package com.yl.paike.teacher.service.impl;

import org.springframework.data.domain.PageImpl;
import com.yl.paike.teacher.dto.CourseCreateDTO;
import com.yl.paike.teacher.dto.CourseDTO;
import com.yl.paike.teacher.entity.Course;
import com.yl.paike.teacher.exception.BusinessException;
import com.yl.paike.teacher.exception.EntityNotFoundException;
import com.yl.paike.teacher.repository.CourseRepository;
import com.yl.paike.teacher.repository.CourseScheduleRepository;
import com.yl.paike.teacher.service.CourseService;
import com.yl.paike.teacher.util.BeanConverter;
import com.yl.paike.teacher.util.Constants;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    
    private final CourseRepository courseRepository;
    private final CourseScheduleRepository scheduleRepository;
    
    @Override
    @Transactional
    public CourseDTO createCourse(CourseCreateDTO createDTO) {
        Course course = new Course();
        course.setCourseName(createDTO.getCourseName());
        course.setAgeGroup(createDTO.getAgeGroup());
        course.setMaxStudents(createDTO.getMaxStudents());
        course.setDescription(createDTO.getDescription());
        course.setIsActive(true);
        
        Course savedCourse = courseRepository.save(course);
        log.info("课程创建成功，课程编号：{}", savedCourse.getCourseCode());
        
        return convertToDTO(savedCourse);
    }
    
    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("课程", id));
        return convertToDTO(course);
    }

    @Override
    public Page<CourseDTO> getCourseList(Integer ageGroup, String keyword, Pageable pageable) {
        Specification<Course> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (ageGroup != null) {
                predicates.add(cb.equal(root.get("ageGroup"), ageGroup));
            }
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(cb.like(root.get("courseName"), "%" + keyword + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<Course> coursePage = courseRepository.findAll(spec, pageable);
        return coursePage.map(this::convertToDTO);
    }



    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseCreateDTO updateDTO) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("课程", id));
        
        course.setCourseName(updateDTO.getCourseName());
        course.setAgeGroup(updateDTO.getAgeGroup());
        course.setMaxStudents(updateDTO.getMaxStudents());
        course.setDescription(updateDTO.getDescription());
        
        Course savedCourse = courseRepository.save(course);
        log.info("课程更新成功，课程ID：{}", id);
        
        return convertToDTO(savedCourse);
    }
    
    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("课程", id));
        
        long scheduleCount = scheduleRepository.findByCourseIdAndStatus(id, 1).size();
        if (scheduleCount > 0) {
            throw new BusinessException("课程有未完成的安排，无法删除");
        }
        
        course.setIsActive(false);
        courseRepository.save(course);
        
        log.info("课程删除成功，课程ID：{}", id);
    }
    
    @Override
    public List<CourseDTO> getAllActiveCourses() {
        List<Course> courses = courseRepository.findByIsActiveTrue();
        return courses.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = BeanConverter.convert(course, CourseDTO.class);
        dto.setAgeGroupName(Constants.AGE_GROUP_NAMES[course.getAgeGroup()]);
        return dto;
    }
}
