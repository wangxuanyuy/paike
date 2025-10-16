package com.yl.paike.teacher.service;

import com.yl.paike.teacher.dto.ConflictCheckResult;
import com.yl.paike.teacher.dto.ConflictWarningDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ConflictDetectionService {
    
    ConflictCheckResult checkTeacherConflicts(List<Long> teacherIds, LocalDate date, Long timeSlotId);
    
    ConflictCheckResult checkClassroomConflicts(Long classroomId, LocalDate date, Long timeSlotId);
    
    Page<ConflictWarningDTO> getConflictWarnings(Integer conflictType, Integer status, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    void resolveConflictWarning(Long warningId, Integer resolution, String remark);
    
    void performFullConflictCheck(LocalDate startDate, LocalDate endDate);
}
