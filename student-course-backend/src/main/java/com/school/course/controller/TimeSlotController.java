// TimeSlotController.java
package com.school.course.controller;

import com.school.course.dto.ApiResponse;
import com.school.course.entity.TimeSlot;
import com.school.course.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/time-slots")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotRepository timeSlotRepository;

    /**
     * 获取所有时间段
     */
    @GetMapping
    public ApiResponse<List<TimeSlot>> getAllTimeSlots() {
        List<TimeSlot> timeSlots = timeSlotRepository.findByIsActiveTrueOrderByDayOfWeekAscStartTimeAsc();
        return ApiResponse.success(timeSlots);
    }
}