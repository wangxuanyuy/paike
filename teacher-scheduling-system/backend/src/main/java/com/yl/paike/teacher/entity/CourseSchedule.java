package com.yl.paike.teacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_COURSE_SCHEDULE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "time_slot_id", nullable = false)
    private Long timeSlotId;

    @Column(name = "classroom_id", nullable = false)
    private Long classroomId;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "status")
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", insertable = false, updatable = false)
    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", insertable = false, updatable = false)
    private Classroom classroom;
}
