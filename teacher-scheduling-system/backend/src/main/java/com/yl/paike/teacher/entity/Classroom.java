package com.yl.paike.teacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Column(name = "classroom_code", unique = true, nullable = false, length = 20)
    private String classroomCode;

    @Column(name = "classroom_name", nullable = false, length = 100)
    private String classroomName;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "facilities", columnDefinition = "TEXT")
    private String facilities;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void generateClassroomCode() {
        if (this.classroomCode == null || this.classroomCode.isEmpty()) {
            this.classroomCode = "CLS" + System.currentTimeMillis();
        }
    }
}
