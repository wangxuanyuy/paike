// 4. CourseSchedule 实体类
package com.school.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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

    @Column(name = "COURSE_ID", nullable = false)
    private Long courseId;

    @Column(name = "TIME_SLOT_ID", nullable = false)
    private Long timeSlotId;

    @Column(name = "CLASSROOM_ID", nullable = false)
    private Long classroomId;

    @Column(name = "SCHEDULE_DATE", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "STATUS")
    private Integer status = 1; // 1-正常 2-取消 3-调课

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // 关联查询
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIME_SLOT_ID", insertable = false, updatable = false)
    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLASSROOM_ID", insertable = false, updatable = false)
    private Classroom classroom;
}
