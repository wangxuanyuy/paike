// 2. Classroom 实体类
package com.school.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "J_CLASSROOMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CLASSROOM_CODE", unique = true, nullable = false, length = 20)
    private String classroomCode;

    @Column(name = "CLASSROOM_NAME", nullable = false, length = 100)
    private String classroomName;

    @Column(name = "MAX_CAPACITY", nullable = false)
    private Integer maxCapacity;

    @Column(name = "LOCATION", length = 200)
    private String location;

    @Column(name = "FACILITIES", columnDefinition = "TEXT")
    private String facilities;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    private void generateClassroomCode() {
        if (this.classroomCode == null) {
            this.classroomCode = "CLS" + System.currentTimeMillis();
        }
    }
}
