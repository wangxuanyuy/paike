package com.yl.paike.teacher.service;

import com.yl.paike.teacher.dto.TimeSlotDTO;

import java.util.List;

public interface TimeSlotService {
    
    List<TimeSlotDTO> getAllActiveTimeSlots();
    
    List<TimeSlotDTO> getTimeSlotsByDayOfWeek(Integer dayOfWeek);
}
