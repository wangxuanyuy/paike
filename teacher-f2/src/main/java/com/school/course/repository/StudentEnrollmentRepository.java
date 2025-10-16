package com.school.course.repository;

import com.school.course.entity.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {
    List<StudentEnrollment> findByStudentIdAndEnrollmentStatus(Long studentId, Integer status);
    
    @Query("SELECT COUNT(e) FROM StudentEnrollment e WHERE e.courseScheduleId = :scheduleId AND e.enrollmentStatus = 1")
    Integer countEnrolledStudents(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT e FROM StudentEnrollment e " +
           "JOIN e.courseSchedule cs " +
           "WHERE e.studentId = :studentId " +
           "AND cs.scheduleDate = :date " +
           "AND cs.timeSlotId = :timeSlotId " +
           "AND e.enrollmentStatus = 1")
    Optional<StudentEnrollment> findStudentTimeConflict(@Param("studentId") Long studentId,
                                                       @Param("date") LocalDate date,
                                                       @Param("timeSlotId") Long timeSlotId);
    
    Optional<StudentEnrollment> findByStudentIdAndCourseScheduleIdAndEnrollmentStatus(
        Long studentId, Long courseScheduleId, Integer enrollmentStatus);
}
