package com.school.course.service;

import com.school.course.entity.*;
import com.school.course.dto.EnrollmentDTO;
import com.school.course.repository.*;
import com.school.course.exception.BusinessException;
import com.school.course.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {
    
    private final StudentEnrollmentRepository enrollmentRepository;
    private final CourseScheduleRepository scheduleRepository;
    private final StudentRepository studentRepository;
    
    /**
     * 学生选课
     */
    public EnrollmentDTO enrollCourse(Long studentId, Long courseScheduleId) {
        // 验证学生是否存在且已通过审核
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("学生不存在"));
        
        if (student.getRegistrationStatus() != 2) {
            throw new BusinessException("学生尚未通过审核，无法选课");
        }
        
        // 验证课程安排是否存在
        CourseSchedule schedule = scheduleRepository.findById(courseScheduleId)
            .orElseThrow(() -> new EntityNotFoundException("课程安排不存在"));
        
        // 检查年龄组匹配
        if (!schedule.getCourse().getAgeGroup().equals(student.getAgeGroup())) {
            throw new BusinessException("学生年龄组不符合课程要求");
        }
        
        // 检查课程容量
        Integer currentCount = enrollmentRepository.countEnrolledStudents(courseScheduleId);
        if (currentCount >= schedule.getCourse().getMaxStudents()) {
            throw new BusinessException("课程已满，无法选课");
        }
        
        // 检查学生时间冲突
        Optional<StudentEnrollment> conflict = enrollmentRepository.findStudentTimeConflict(
            studentId, schedule.getScheduleDate(), schedule.getTimeSlotId());
        
        if (conflict.isPresent()) {
            throw new BusinessException("学生在该时间段已有课程安排");
        }
        
        // 检查是否已经选过该课程
        Optional<StudentEnrollment> existing = enrollmentRepository
            .findByStudentIdAndCourseScheduleIdAndEnrollmentStatus(studentId, courseScheduleId, 1);
        
        if (existing.isPresent()) {
            throw new BusinessException("已经选择过该课程");
        }
        
        // 创建选课记录
        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseScheduleId(courseScheduleId);
        enrollment.setEnrollmentStatus(1);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        
        StudentEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        
        log.info("学生选课成功，学生ID：{}，课程安排ID：{}", studentId, courseScheduleId);
        
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(savedEnrollment.getStudentId());
        dto.setCourseScheduleId(savedEnrollment.getCourseScheduleId());
        dto.setEnrollmentStatus(savedEnrollment.getEnrollmentStatus());
        dto.setEnrollmentDate(savedEnrollment.getEnrollmentDate());
        
        return dto;
    }
    
    /**
     * 取消选课
     */
    public void cancelEnrollment(Long studentId, Long courseScheduleId) {
        StudentEnrollment enrollment = enrollmentRepository
            .findByStudentIdAndCourseScheduleIdAndEnrollmentStatus(studentId, courseScheduleId, 1)
            .orElseThrow(() -> new EntityNotFoundException("选课记录不存在"));
        
        enrollment.setEnrollmentStatus(2); // 已取消
        enrollmentRepository.save(enrollment);
        
        log.info("取消选课成功，学生ID：{}，课程安排ID：{}", studentId, courseScheduleId);
    }
}
