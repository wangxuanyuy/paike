package com.school.course.repository;

import com.school.course.entity.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
    List<CourseSchedule> findByScheduleDateAndStatus(LocalDate date, Integer status);
    
    @Query("SELECT cs FROM CourseSchedule cs " +
           "JOIN FETCH cs.course c " +
           "JOIN FETCH cs.timeSlot ts " +
           "JOIN FETCH cs.classroom cl " +
           "WHERE cs.scheduleDate BETWEEN :startDate AND :endDate " +
           "AND cs.status = 1 " +
           "ORDER BY ts.dayOfWeek, ts.startTime")
    List<CourseSchedule> findWeekSchedule(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.timeSlotId = :timeSlotId " +
           "AND cs.classroomId = :classroomId AND cs.scheduleDate = :date AND cs.status = 1")
    Optional<CourseSchedule> findConflictSchedule(@Param("timeSlotId") Long timeSlotId,
                                                @Param("classroomId") Long classroomId,
                                                @Param("date") LocalDate date);
}
