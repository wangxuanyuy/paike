package com.school.course.service;

import com.school.course.entity.Student;
import com.school.course.dto.StudentDTO;
import com.school.course.dto.StudentRegistrationDTO;
import com.school.course.repository.StudentRepository;
import com.school.course.exception.BusinessException;
import com.school.course.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    /**
     * 学生注册
     */
    public StudentDTO registerStudent(StudentRegistrationDTO registerDTO) {
        // 检查手机号是否已注册
        Optional<Student> existingStudent = studentRepository.findByParentPhone(registerDTO.getParentPhone());
        if (existingStudent.isPresent()) {
            throw new BusinessException("该手机号已注册过学生");
        }
        
        Student student = new Student();
        BeanUtils.copyProperties(registerDTO, student);
        student.setRegistrationStatus(2); // 直接通过审核
        
        Student savedStudent = studentRepository.save(student);
        log.info("学生注册成功，学生编号：{}", savedStudent.getStudentCode());
        
        return convertToDTO(savedStudent);
    }
    
    /**
     * 根据ID查询学生
     */
    public StudentDTO getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("学生不存在"));
        return convertToDTO(student);
    }
    
    /**
     * 获取所有学生
     */
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findActiveStudents();
        return students.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * 根据年龄组查询学生
     */
    public List<StudentDTO> getStudentsByAgeGroup(Integer ageGroup) {
        List<Student> students = studentRepository.findByAgeGroup(ageGroup);
        return students.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * 更新学生信息
     */
    public StudentDTO updateStudent(Long studentId, StudentDTO updateDTO) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("学生不存在"));
        
        if (updateDTO.getStudentName() != null) {
            student.setStudentName(updateDTO.getStudentName());
        }
        if (updateDTO.getAge() != null) {
            student.setAge(updateDTO.getAge());
            // 重新计算年龄组
            updateAgeGroup(student);
        }
        if (updateDTO.getParentName() != null) {
            student.setParentName(updateDTO.getParentName());
        }
        if (updateDTO.getParentPhone() != null) {
            student.setParentPhone(updateDTO.getParentPhone());
        }
        if (updateDTO.getParentEmail() != null) {
            student.setParentEmail(updateDTO.getParentEmail());
        }
        
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }
    
    private void updateAgeGroup(Student student) {
        Integer age = student.getAge();
        if (age >= 1 && age <= 3) {
            student.setAgeGroup(1);
        } else if (age > 3 && age <= 6) {
            student.setAgeGroup(2);
        } else if (age > 6 && age <= 9) {
            student.setAgeGroup(3);
        } else if (age > 9 && age <= 12) {
            student.setAgeGroup(4);
        }
    }
    
    /**
     * 实体转DTO
     */
    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        BeanUtils.copyProperties(student, dto);
        return dto;
    }
}
