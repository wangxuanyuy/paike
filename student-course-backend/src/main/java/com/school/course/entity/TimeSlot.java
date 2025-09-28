package com.school.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_TIME_SLOTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SLOT_NAME", nullable = false)
    private String slotName;

    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalTime endTime;

    @Column(name = "DAY_OF_WEEK", nullable = false)
    private Integer dayOfWeek; // 1-7 (周一到周日)

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}