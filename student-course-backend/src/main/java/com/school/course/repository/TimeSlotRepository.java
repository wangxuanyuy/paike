package com.school.course.repository;

import com.school.course.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDayOfWeekAndIsActiveTrue(Integer dayOfWeek);
    List<TimeSlot> findByIsActiveTrueOrderByDayOfWeekAscStartTimeAsc();
}
