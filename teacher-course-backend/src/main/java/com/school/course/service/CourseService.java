package com.school.course.service;

import com.school.course.entity.*;
import com.school.course.dto.*;
import com.school.course.repository.*;
import com.school.course.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    
    private final CourseScheduleRepository scheduleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final StudentEnrollmentRepository enrollmentRepository;
    
    private static final String[] DAY_NAMES = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    
    /**
     * 获取课程表视图（按周显示）
     */
    public List<CourseScheduleViewDTO> getWeekScheduleView(Integer studentAgeGroup, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        List<CourseSchedule> schedules = scheduleRepository.findWeekSchedule(startDate, endDate);
        
        Map<Integer, CourseScheduleViewDTO> dayMap = new LinkedHashMap<>();
        
        // 初始化一周的数据结构
        for (int i = 1; i <= 7; i++) {
            CourseScheduleViewDTO dayView = new CourseScheduleViewDTO();
            dayView.setDayOfWeek(i);
            dayView.setDayName(DAY_NAMES[i]);
            dayView.setTimeSlots(new ArrayList<>());
            dayMap.put(i, dayView);
        }
        
        // 获取所有时间段
        List<TimeSlot> timeSlots = timeSlotRepository.findByIsActiveTrueOrderByDayOfWeekAscStartTimeAsc();
        
        // 为每个时间段创建视图
        Map<String, TimeSlotViewDTO> timeSlotMap = new HashMap<>();
        for (TimeSlot timeSlot : timeSlots) {
            String key = timeSlot.getDayOfWeek() + "_" + timeSlot.getId();
            TimeSlotViewDTO timeSlotView = new TimeSlotViewDTO();
            timeSlotView.setTimeSlotId(timeSlot.getId());
            timeSlotView.setSlotName(timeSlot.getSlotName());
            timeSlotView.setStartTime(timeSlot.getStartTime());
            timeSlotView.setEndTime(timeSlot.getEndTime());
            timeSlotView.setCourses(new ArrayList<>());
            
            timeSlotMap.put(key, timeSlotView);
            dayMap.get(timeSlot.getDayOfWeek()).getTimeSlots().add(timeSlotView);
        }
        
        // 填充课程信息
        for (CourseSchedule schedule : schedules) {
            String key = schedule.getTimeSlot().getDayOfWeek() + "_" + schedule.getTimeSlotId();
            TimeSlotViewDTO timeSlotView = timeSlotMap.get(key);
            
            if (timeSlotView != null) {
                CourseInfoDTO courseInfo = buildCourseInfo(schedule, studentAgeGroup);
                timeSlotView.getCourses().add(courseInfo);
            }
        }
        
        return new ArrayList<>(dayMap.values());
    }
    
    /**
     * 构建课程信息
     */
    private CourseInfoDTO buildCourseInfo(CourseSchedule schedule, Integer studentAgeGroup) {
        CourseInfoDTO courseInfo = new CourseInfoDTO();
        courseInfo.setCourseId(schedule.getCourseId());
        courseInfo.setScheduleId(schedule.getId());
        courseInfo.setCourseName(schedule.getCourse().getCourseName());
        courseInfo.setClassroomName(schedule.getClassroom().getClassroomName());
        courseInfo.setMaxStudents(schedule.getCourse().getMaxStudents());
        courseInfo.setAgeGroup(schedule.getCourse().getAgeGroup());
        
        // 获取当前选课人数
        Integer currentStudents = enrollmentRepository.countEnrolledStudents(schedule.getId());
        courseInfo.setCurrentStudents(currentStudents);
        
        // 获取教师信息
        String teacherNames = schedule.getCourse().getTeacherNames();
        if (teacherNames != null) {
            courseInfo.setTeacherNames(Arrays.asList(teacherNames.split(",")));
        } else {
            courseInfo.setTeacherNames(Arrays.asList("张三", "李四")); // 默认教师
        }
        
        // 判断课程状态和是否可选
        String status = determineStatus(schedule, currentStudents, studentAgeGroup);
        courseInfo.setStatus(status);
        courseInfo.setCanEnroll("AVAILABLE".equals(status) && 
                              schedule.getCourse().getAgeGroup().equals(studentAgeGroup));
        
        return courseInfo;
    }
    
    /**
     * 确定课程状态
     */
    private String determineStatus(CourseSchedule schedule, Integer currentStudents, Integer studentAgeGroup) {
        Course course = schedule.getCourse();
        
        // 年龄组不匹配，显示灰色
        if (!course.getAgeGroup().equals(studentAgeGroup)) {
            return "DISABLED";
        }
        
        // 已满，显示红色
        if (currentStudents >= course.getMaxStudents()) {
            return "FULL";
        }
        
        // 有人选课但未满，显示黄色
        if (currentStudents > 0) {
            return "AVAILABLE";
        }
        
        // 无人选课，显示白色
        return "EMPTY";
    }
}
