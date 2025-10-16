package com.yl.paike.teacher.controller;

import com.yl.paike.teacher.dto.CourseCreateDTO;
import com.yl.paike.teacher.dto.CourseDTO;
import com.yl.paike.teacher.dto.PageResult;
import com.yl.paike.teacher.dto.Result;
import com.yl.paike.teacher.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    
    private final CourseService courseService;
    
    @PostMapping
    public Result<CourseDTO> createCourse(@Valid @RequestBody CourseCreateDTO createDTO) {
        log.info("创建课程: {}", createDTO.getCourseName());
        CourseDTO course = courseService.createCourse(createDTO);
        return Result.success(course, "课程创建成功");
    }
    
    @GetMapping("/{id}")
    public Result<CourseDTO> getCourse(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return Result.success(course);
    }
    
    @GetMapping
    public Result<PageResult<CourseDTO>> getCourseList(
            @RequestParam(required = false) Integer ageGroup,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CourseDTO> coursePage = courseService.getCourseList(ageGroup, keyword, pageable);
        
        PageResult<CourseDTO> pageResult = new PageResult<>();
        pageResult.setContent(coursePage.getContent());
        pageResult.setTotalElements(coursePage.getTotalElements());
        pageResult.setTotalPages(coursePage.getTotalPages());
        pageResult.setPage(page);
        pageResult.setSize(size);
        
        return Result.success(pageResult);
    }
    
    @GetMapping("/active")
    public Result<List<CourseDTO>> getActiveCourses() {
        List<CourseDTO> courses = courseService.getAllActiveCourses();
        return Result.success(courses);
    }
    
    @PutMapping("/{id}")
    public Result<CourseDTO> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateDTO updateDTO) {
        log.info("更新课程: {}", id);
        CourseDTO course = courseService.updateCourse(id, updateDTO);
        return Result.success(course, "更新成功");
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        log.info("删除课程: {}", id);
        courseService.deleteCourse(id);
        return Result.success(null, "删除成功");
    }
}
