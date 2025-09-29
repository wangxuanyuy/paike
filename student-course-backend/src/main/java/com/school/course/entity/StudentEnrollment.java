// 7. StudentEnrollment 实体类
package com.school.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "S_STUDENT_ENROLLMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STUDENT_ID", nullable = false)
    private Long studentId;

    @Column(name = "COURSE_SCHEDULE_ID", nullable = false)
    private Long courseScheduleId;

    @Column(name = "ENROLLMENT_STATUS")
    private Integer enrollmentStatus = 1; // 1-已选 2-已取消

    @Column(name = "ENROLLMENT_DATE")
    private LocalDateTime enrollmentDate;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // 关联查询
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_SCHEDULE_ID", insertable = false, updatable = false)
    private CourseSchedule courseSchedule;
}
