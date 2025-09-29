// 3. Course 实体类
package com.school.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "K_COURSES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COURSE_CODE", unique = true, nullable = false, length = 20)
    private String courseCode;

    @Column(name = "COURSE_NAME", nullable = false, length = 100)
    private String courseName;

    @Column(name = "AGE_GROUP", nullable = false)
    private Integer ageGroup; // 对应数据库的 tinyint

    @Column(name = "MAX_STUDENTS", nullable = false)
    private Integer maxStudents;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "TEACHER_NAMES", length = 200)
    private String teacherNames; // 教师姓名（多个用逗号分隔）

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    private void generateCourseCode() {
        if (this.courseCode == null) {
            this.courseCode = "COU" + System.currentTimeMillis();
        }
    }
}
