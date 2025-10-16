package com.yl.paike.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import com.yl.paike.teacher.dto.*;
import com.yl.paike.teacher.entity.*;
import com.yl.paike.teacher.exception.BusinessException;
import com.yl.paike.teacher.exception.EntityNotFoundException;
import com.yl.paike.teacher.repository.*;
import com.yl.paike.teacher.service.ConflictDetectionService;
import com.yl.paike.teacher.service.ScheduleService;
import com.yl.paike.teacher.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class ScheduleServiceImpl implements ScheduleService {
    
    private final CourseScheduleRepository scheduleRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherAssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ConflictDetectionService conflictDetectionService;
    
    @Override
    @Transactional
    public CourseScheduleDTO createSchedule(CourseScheduleCreateDTO createDTO) {
        Course course = courseRepository.findById(createDTO.getCourseId())
            .orElseThrow(() -> new EntityNotFoundException("课程", createDTO.getCourseId()));
        
        Classroom classroom = classroomRepository.findById(createDTO.getClassroomId())
            .orElseThrow(() -> new EntityNotFoundException("教室", createDTO.getClassroomId()));
        
        TimeSlot timeSlot = timeSlotRepository.findById(createDTO.getTimeSlotId())
            .orElseThrow(() -> new EntityNotFoundException("时间段", createDTO.getTimeSlotId()));
        
        ConflictCheckResult classroomConflict = conflictDetectionService.checkClassroomConflicts(
            createDTO.getClassroomId(), createDTO.getScheduleDate(), createDTO.getTimeSlotId());
        
        if (classroomConflict.getHasConflicts()) {
            throw new BusinessException("教室时间冲突：" + classroomConflict.getConflictDescription());
        }
        
        CourseSchedule schedule = new CourseSchedule();
        schedule.setCourseId(createDTO.getCourseId());
        schedule.setTimeSlotId(createDTO.getTimeSlotId());
        schedule.setClassroomId(createDTO.getClassroomId());
        schedule.setScheduleDate(createDTO.getScheduleDate());
        schedule.setStatus(Constants.SCHEDULE_STATUS_NORMAL);
        
        CourseSchedule savedSchedule = scheduleRepository.save(schedule);
        log.info("课程安排创建成功，安排ID：{}", savedSchedule.getId());
        
        return convertToDTO(savedSchedule);
    }
    
    @Override
    @Transactional
    public ScheduleAssignmentDTO assignTeachers(ScheduleAssignmentRequestDTO requestDTO) {
        CourseSchedule schedule = scheduleRepository.findById(requestDTO.getCourseScheduleId())
            .orElseThrow(() -> new EntityNotFoundException("课程安排", requestDTO.getCourseScheduleId()));
        
        if (requestDTO.getTeacherIds().size() < 1 || requestDTO.getTeacherIds().size() > 3) {
            throw new BusinessException("每堂课需要1-3名教师");
        }
        
        if (!requestDTO.getTeacherIds().contains(requestDTO.getMainTeacherId())) {
            throw new BusinessException("主讲教师必须在教师列表中");
        }
        
        List<Teacher> teachers = new ArrayList<>();
        for (Long teacherId : requestDTO.getTeacherIds()) {
            Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("教师", teacherId));
            
            if (!teacher.getIsActive()) {
                throw new BusinessException("教师已禁用：" + teacher.getTeacherName());
            }
            
            if (!teacher.getAgeGroupList().contains(schedule.getCourse().getAgeGroup())) {
                throw new BusinessException("教师年龄组不匹配：" + teacher.getTeacherName());
            }
            
            teachers.add(teacher);
        }
        
        ConflictCheckResult conflictResult = conflictDetectionService.checkTeacherConflicts(
            requestDTO.getTeacherIds(), 
            schedule.getScheduleDate(), 
            schedule.getTimeSlotId());
        
        if (conflictResult.getHasConflicts()) {
            throw new BusinessException("教师时间冲突：" + conflictResult.getConflictDescription());
        }
        
        assignmentRepository.deleteByCourseScheduleId(requestDTO.getCourseScheduleId());
        
        List<TeacherAssignment> assignments = new ArrayList<>();
        for (Long teacherId : requestDTO.getTeacherIds()) {
            TeacherAssignment assignment = new TeacherAssignment();
            assignment.setTeacherId(teacherId);
            assignment.setCourseScheduleId(requestDTO.getCourseScheduleId());
            assignment.setIsMainTeacher(teacherId.equals(requestDTO.getMainTeacherId()));
            assignments.add(assignment);
        }
        
        List<TeacherAssignment> savedAssignments = assignmentRepository.saveAll(assignments);
        
        log.info("教师分配成功，课程安排ID：{}，教师数量：{}", 
                requestDTO.getCourseScheduleId(), requestDTO.getTeacherIds().size());
        
        return buildScheduleAssignmentDTO(savedAssignments, schedule);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CourseScheduleDTO> getScheduleList(LocalDate startDate, LocalDate endDate,
                                                   Integer status, Pageable pageable) {
        // 先查询所有数据（带关联）
        List<CourseSchedule> allSchedules = scheduleRepository.findAllWithDetails(startDate, endDate, status);

        // 手动分页
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allSchedules.size());
        List<CourseSchedule> pageContent = allSchedules.subList(start, end);

        // 转换为DTO
        List<CourseScheduleDTO> dtoList = pageContent.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, allSchedules.size());
    }

   /* @Override
    @Transactional(readOnly = true)
    public Page<CourseScheduleDTO> getScheduleList(LocalDate startDate, LocalDate endDate,
                                                   Integer status, Pageable pageable) {
        Specification<CourseSchedule> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 注意：不要在这里使用 fetch，因为会和分页冲突

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("scheduleDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("scheduleDate"), endDate));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<CourseSchedule> schedulePage = scheduleRepository.findAll(spec, pageable);

        // 在转换时单独查询关联对象
        return schedulePage.map(schedule -> {
            CourseScheduleDTO dto = new CourseScheduleDTO();
            dto.setId(schedule.getId());
            dto.setCourseId(schedule.getCourseId());
            dto.setTimeSlotId(schedule.getTimeSlotId());
            dto.setClassroomId(schedule.getClassroomId());
            dto.setScheduleDate(schedule.getScheduleDate());
            dto.setStatus(schedule.getStatus());

            // 手动查询关联对象
            courseRepository.findById(schedule.getCourseId()).ifPresent(course -> {
                dto.setCourseName(course.getCourseName());
                dto.setAgeGroup(course.getAgeGroup());
                dto.setMaxStudents(course.getMaxStudents());
            });

            timeSlotRepository.findById(schedule.getTimeSlotId()).ifPresent(timeSlot -> {
                dto.setTimeSlotName(timeSlot.getSlotName());
                dto.setStartTime(timeSlot.getStartTime());
                dto.setEndTime(timeSlot.getEndTime());
            });

            classroomRepository.findById(schedule.getClassroomId()).ifPresent(classroom -> {
                dto.setClassroomName(classroom.getClassroomName());
            });

            // 获取教师名称列表
            List<TeacherAssignment> assignments = assignmentRepository.findByCourseScheduleId(schedule.getId());
            List<String> teacherNames = assignments.stream()
                    .map(a -> {
                        return teacherRepository.findById(a.getTeacherId())
                                .map(Teacher::getTeacherName)
                                .orElse("未知教师");
                    })
                    .collect(Collectors.toList());
            dto.setTeacherNames(teacherNames);

            // TODO: 获取当前学生数
            dto.setCurrentStudents(0);

            return dto;
        });
    }
*/
    @Override
    @Transactional
    public void cancelSchedule(Long scheduleId, String reason) {
        CourseSchedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new EntityNotFoundException("课程安排", scheduleId));
        
        schedule.setStatus(Constants.SCHEDULE_STATUS_CANCELLED);
        scheduleRepository.save(schedule);
        
        assignmentRepository.deleteByCourseScheduleId(scheduleId);
        
        log.info("课程取消成功，课程安排ID：{}，取消原因：{}", scheduleId, reason);
    }
    
    @Override
    public List<CourseScheduleDTO> getTeacherSchedules(Long teacherId, LocalDate startDate, LocalDate endDate) {
        List<TeacherAssignment> assignments = assignmentRepository.findByTeacherIdAndDateRange(
            teacherId, startDate, endDate);
        
        return assignments.stream()
            .map(assignment -> convertToDTO(assignment.getCourseSchedule()))
            .collect(Collectors.toList());
    }
    
    private CourseScheduleDTO convertToDTO(CourseSchedule schedule) {
        CourseScheduleDTO dto = new CourseScheduleDTO();
        dto.setId(schedule.getId());
        dto.setCourseId(schedule.getCourseId());
        dto.setCourseName(schedule.getCourse().getCourseName());
        dto.setAgeGroup(schedule.getCourse().getAgeGroup());
        dto.setTimeSlotId(schedule.getTimeSlotId());
        dto.setTimeSlotName(schedule.getTimeSlot().getSlotName());
        dto.setStartTime(schedule.getTimeSlot().getStartTime());
        dto.setEndTime(schedule.getTimeSlot().getEndTime());
        dto.setClassroomId(schedule.getClassroomId());
        dto.setClassroomName(schedule.getClassroom().getClassroomName());
        dto.setScheduleDate(schedule.getScheduleDate());
        dto.setStatus(schedule.getStatus());
        dto.setMaxStudents(schedule.getCourse().getMaxStudents());
        
        List<TeacherAssignment> assignments = assignmentRepository.findByCourseScheduleId(schedule.getId());
        List<String> teacherNames = assignments.stream()
            .map(a -> a.getTeacher().getTeacherName())
            .collect(Collectors.toList());
        dto.setTeacherNames(teacherNames);
        
        dto.setCurrentStudents(0);
        
        return dto;
    }
    
    private ScheduleAssignmentDTO buildScheduleAssignmentDTO(List<TeacherAssignment> assignments, CourseSchedule schedule) {
        ScheduleAssignmentDTO dto = new ScheduleAssignmentDTO();
        dto.setCourseScheduleId(schedule.getId());
        dto.setCourseName(schedule.getCourse().getCourseName());
        dto.setClassroomName(schedule.getClassroom().getClassroomName());
        dto.setScheduleDate(schedule.getScheduleDate());
        dto.setTimeSlotName(schedule.getTimeSlot().getSlotName());
        
        List<TeacherAssignmentDetailDTO> teacherDetails = assignments.stream()
            .map(assignment -> {
                TeacherAssignmentDetailDTO detail = new TeacherAssignmentDetailDTO();
                detail.setAssignmentId(assignment.getId());
                detail.setTeacherId(assignment.getTeacherId());
                detail.setTeacherName(assignment.getTeacher().getTeacherName());
                detail.setIsMainTeacher(assignment.getIsMainTeacher());
                return detail;
            })
            .collect(Collectors.toList());
        
        dto.setTeacherDetails(teacherDetails);
        
        return dto;
    }
}
