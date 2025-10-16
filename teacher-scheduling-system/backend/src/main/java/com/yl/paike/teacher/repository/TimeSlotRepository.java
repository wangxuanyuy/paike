package com.yl.paike.teacher.repository;

import com.yl.paike.teacher.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    
    List<TimeSlot> findByIsActiveTrueOrderByDayOfWeekAscStartTimeAsc();
    
    List<TimeSlot> findByDayOfWeekAndIsActiveTrue(Integer dayOfWeek);
    
    List<TimeSlot> findByIsActiveTrue();
}
