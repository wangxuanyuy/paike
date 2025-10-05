package com.yl.paike.teacher.repository;

import com.yl.paike.teacher.entity.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long>, JpaSpecificationExecutor<CourseSchedule> {
    
    List<CourseSchedule> findByScheduleDateAndStatus(LocalDate date, Integer status);
    
    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.scheduleDate BETWEEN :startDate AND :endDate AND cs.status = 1 ORDER BY cs.scheduleDate, cs.timeSlotId")
    List<CourseSchedule> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.timeSlotId = :timeSlotId AND cs.classroomId = :classroomId AND cs.scheduleDate = :date AND cs.status = 1")
    Optional<CourseSchedule> findConflictSchedule(@Param("timeSlotId") Long timeSlotId, @Param("classroomId") Long classroomId, @Param("date") LocalDate date);
    
    List<CourseSchedule> findByCourseIdAndStatus(Long courseId, Integer status);
    /**
     * 查询所有课程安排（不使用 JOIN FETCH，以避免 Hibernate 在某些情况下抛出
     * "join fetching, but the owner of the fetched association was not present in the select list" 错误）。
     * 关联对象将在 Service 层的事务中按需懒加载。
     */
    @Query("SELECT cs FROM CourseSchedule cs " +
            "WHERE (:startDate IS NULL OR cs.scheduleDate >= :startDate) " +
            "AND (:endDate IS NULL OR cs.scheduleDate <= :endDate) " +
            "AND (:status IS NULL OR cs.status = :status) " +
            "ORDER BY cs.scheduleDate DESC, cs.timeSlotId")
    List<CourseSchedule> findAllWithDetails(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            @Param("status") Integer status);
}
