package com.yl.paike.teacher.repository;

import com.yl.paike.teacher.entity.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, Long> {
    
    List<TeacherAssignment> findByTeacherId(Long teacherId);
    
    List<TeacherAssignment> findByCourseScheduleId(Long courseScheduleId);
    
    @Modifying
    @Query("DELETE FROM TeacherAssignment ta WHERE ta.courseScheduleId = :scheduleId")
    void deleteByCourseScheduleId(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT ta FROM TeacherAssignment ta JOIN ta.courseSchedule cs WHERE ta.teacherId = :teacherId AND cs.scheduleDate BETWEEN :startDate AND :endDate AND cs.status = 1")
    List<TeacherAssignment> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ta FROM TeacherAssignment ta JOIN ta.courseSchedule cs WHERE ta.teacherId = :teacherId AND cs.scheduleDate = :date AND cs.timeSlotId = :timeSlotId AND cs.status = 1")
    List<TeacherAssignment> findConflictingAssignments(@Param("teacherId") Long teacherId, @Param("date") LocalDate date, @Param("timeSlotId") Long timeSlotId);
    
    boolean existsByTeacherIdAndCourseScheduleId(Long teacherId, Long courseScheduleId);
}
