package com.yl.paike.teacher.controller;

import com.yl.paike.teacher.dto.Result;
import com.yl.paike.teacher.dto.TimeSlotDTO;
import com.yl.paike.teacher.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-slots")
@RequiredArgsConstructor
public class TimeSlotController {
    
    private final TimeSlotService timeSlotService;
    
    @GetMapping
    public Result<List<TimeSlotDTO>> getAllTimeSlots() {
        List<TimeSlotDTO> timeSlots = timeSlotService.getAllActiveTimeSlots();
        return Result.success(timeSlots);
    }
    
    @GetMapping("/day/{dayOfWeek}")
    public Result<List<TimeSlotDTO>> getTimeSlotsByDay(@PathVariable Integer dayOfWeek) {
        List<TimeSlotDTO> timeSlots = timeSlotService.getTimeSlotsByDayOfWeek(dayOfWeek);
        return Result.success(timeSlots);
    }
}
