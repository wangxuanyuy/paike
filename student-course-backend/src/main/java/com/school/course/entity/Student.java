// 1. Student 实体类
package com.school.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "S_STUDENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STUDENT_CODE", unique = true, nullable = false, length = 20)
    private String studentCode;

    @Column(name = "STUDENT_NAME", nullable = false, length = 50)
    private String studentName;

    @Column(name = "AGE", nullable = false)
    private Integer age; // 对应数据库的 tinyint

    @Column(name = "AGE_GROUP", nullable = false)
    private Integer ageGroup; // 对应数据库的 tinyint

    @Column(name = "PARENT_NAME", nullable = false, length = 50)
    private String parentName;

    @Column(name = "PARENT_PHONE", nullable = false, length = 15)
    private String parentPhone;

    @Column(name = "PARENT_EMAIL", length = 100)
    private String parentEmail;

    @Column(name = "REGISTRATION_STATUS")
    private Integer registrationStatus = 2; // 1-待审核 2-已通过 3-已拒绝

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    private void generateStudentCode() {
        if (this.studentCode == null) {
            this.studentCode = "STU" + System.currentTimeMillis();
        }
        // 根据年龄自动设置年龄组
        if (this.age != null) {
            if (this.age >= 1 && this.age <= 3) {
                this.ageGroup = 1; // 学前班
            } else if (this.age > 3 && this.age <= 6) {
                this.ageGroup = 2; // 小班
            } else if (this.age > 6 && this.age <= 9) {
                this.ageGroup = 3; // 中班
            } else if (this.age > 9 && this.age <= 12) {
                this.ageGroup = 4; // 大班
            }
        }
    }
}
