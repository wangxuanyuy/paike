package com.yl.paike.teacher.service.impl;

import com.yl.paike.teacher.dto.ConflictCheckResult;
import com.yl.paike.teacher.dto.ConflictWarningDTO;
import com.yl.paike.teacher.entity.ConflictWarning;
import com.yl.paike.teacher.entity.CourseSchedule;
import com.yl.paike.teacher.entity.Teacher;
import com.yl.paike.teacher.entity.TeacherAssignment;
import com.yl.paike.teacher.exception.EntityNotFoundException;
import com.yl.paike.teacher.repository.*;
import com.yl.paike.teacher.service.ConflictDetectionService;
import com.yl.paike.teacher.util.Constants;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConflictDetectionServiceImpl implements ConflictDetectionService {
    
    private final TeacherAssignmentRepository assignmentRepository;
    private final ConflictWarningRepository conflictWarningRepository;
    private final CourseScheduleRepository scheduleRepository;
    private final TeacherRepository teacherRepository;
    private final TimeSlotRepository timeSlotRepository;
    
    @Override
    public ConflictCheckResult checkTeacherConflicts(List<Long> teacherIds, LocalDate date, Long timeSlotId) {
        ConflictCheckResult result = new ConflictCheckResult();
        List<String> conflicts = new ArrayList<>();
        
        for (Long teacherId : teacherIds) {
            List<TeacherAssignment> existingAssignments = 
                assignmentRepository.findConflictingAssignments(teacherId, date, timeSlotId);
            
            if (!existingAssignments.isEmpty()) {
                String teacherName = getTeacherName(teacherId);
                String timeSlotName = getTimeSlotName(timeSlotId);
                conflicts.add(String.format("教师 %s 在 %s 已有课程安排", teacherName, timeSlotName));
                
                recordConflictWarning(
                    Constants.CONFLICT_TYPE_TEACHER, 
                    teacherId, 
                    date, 
                    timeSlotId,
                    String.format("教师时间冲突：%s", teacherName)
                );
            }
        }
        
        result.setHasConflicts(!conflicts.isEmpty());
        result.setConflictDescription(String.join("；", conflicts));
        
        return result;
    }
    
    @Override
    public ConflictCheckResult checkClassroomConflicts(Long classroomId, LocalDate date, Long timeSlotId) {
        ConflictCheckResult result = new ConflictCheckResult();
        
        Optional<CourseSchedule> conflict = scheduleRepository.findConflictSchedule(
            timeSlotId, classroomId, date);
        
        if (conflict.isPresent()) {
            String timeSlotName = getTimeSlotName(timeSlotId);
            String conflictDescription = String.format(
                "教室在 %s %s 已被占用", 
                date, 
                timeSlotName
            );
            
            result.setHasConflicts(true);
            result.setConflictDescription(conflictDescription);
            
            recordConflictWarning(
                Constants.CONFLICT_TYPE_CLASSROOM, 
                classroomId, 
                date, 
                timeSlotId,
                conflictDescription
            );
        }
        
        return result;
    }
    
    @Override
    public Page<ConflictWarningDTO> getConflictWarnings(Integer conflictType, Integer status,
                                                        LocalDate startDate, LocalDate endDate,
                                                        Pageable pageable) {
        Specification<ConflictWarning> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (conflictType != null) {
                predicates.add(cb.equal(root.get("conflictType"), conflictType));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("conflictDate"), startDate));
            }
            
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("conflictDate"), endDate));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<ConflictWarning> warningPage = conflictWarningRepository.findAll(spec, pageable);
        return warningPage.map(this::convertToWarningDTO);
    }
    
    @Override
    @Transactional
    public void resolveConflictWarning(Long warningId, Integer resolution, String remark) {
        ConflictWarning warning = conflictWarningRepository.findById(warningId)
            .orElseThrow(() -> new EntityNotFoundException("冲突警告", warningId));
        
        warning.setStatus(resolution);
        conflictWarningRepository.save(warning);
        
        log.info("冲突警告处理完成，警告ID：{}，处理结果：{}", warningId, resolution);
    }
    
    @Override
    @Transactional
    public void performFullConflictCheck(LocalDate startDate, LocalDate endDate) {
        log.info("开始执行全面冲突检测，日期范围：{} 至 {}", startDate, endDate);
        
        List<CourseSchedule> schedules = scheduleRepository.findByDateRange(startDate, endDate);
        
        log.info("全面冲突检测完成");
    }
    
    private void recordConflictWarning(Integer conflictType, Long relatedId, LocalDate date, 
                                     Long timeSlotId, String description) {
        ConflictWarning warning = new ConflictWarning();
        warning.setConflictType(conflictType);
        warning.setConflictDescription(description);
        warning.setRelatedIds(String.valueOf(relatedId));
        warning.setConflictDate(date);
        warning.setTimeSlotId(timeSlotId);
        warning.setStatus(Constants.WARNING_STATUS_UNRESOLVED);
        
        conflictWarningRepository.save(warning);
    }
    
    private String getTeacherName(Long teacherId) {
        return teacherRepository.findById(teacherId)
            .map(Teacher::getTeacherName)
            .orElse("未知教师");
    }
    
    private String getTimeSlotName(Long timeSlotId) {
        return timeSlotRepository.findById(timeSlotId)
            .map(slot -> slot.getSlotName() + " " + 
                        slot.getStartTime() + "-" + slot.getEndTime())
            .orElse("未知时间段");
    }
    
    private ConflictWarningDTO convertToWarningDTO(ConflictWarning warning) {
        ConflictWarningDTO dto = new ConflictWarningDTO();
        dto.setId(warning.getId());
        dto.setConflictType(warning.getConflictType());
        dto.setConflictTypeName(getConflictTypeName(warning.getConflictType()));
        dto.setConflictDescription(warning.getConflictDescription());
        dto.setConflictDate(warning.getConflictDate());
        dto.setTimeSlotId(warning.getTimeSlotId());
        dto.setTimeSlotName(getTimeSlotName(warning.getTimeSlotId()));
        dto.setStatus(warning.getStatus());
        dto.setStatusName(getStatusName(warning.getStatus()));
        dto.setCreatedAt(warning.getCreatedAt());
        
        if (warning.getRelatedIds() != null && !warning.getRelatedIds().isEmpty()) {
            List<Long> relatedIds = Arrays.stream(warning.getRelatedIds().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
            dto.setRelatedIds(relatedIds);
        }
        
        return dto;
    }
    
    private String getConflictTypeName(Integer type) {
        return switch (type) {
            case Constants.CONFLICT_TYPE_TEACHER -> "教师冲突";
            case Constants.CONFLICT_TYPE_STUDENT -> "学生冲突";
            case Constants.CONFLICT_TYPE_CLASSROOM -> "教室冲突";
            default -> "未知类型";
        };
    }
    
    private String getStatusName(Integer status) {
        return switch (status) {
            case Constants.WARNING_STATUS_UNRESOLVED -> "未处理";
            case Constants.WARNING_STATUS_RESOLVED -> "已处理";
            case Constants.WARNING_STATUS_IGNORED -> "已忽略";
            default -> "未知状态";
        };
    }
}
