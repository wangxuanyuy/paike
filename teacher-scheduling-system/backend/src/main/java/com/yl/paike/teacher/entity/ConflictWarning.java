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
@Table(name = "SYS_CONFLICT_WARNINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConflictWarning {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conflict_type", nullable = false)
    private Integer conflictType;

    @Column(name = "conflict_description", nullable = false, columnDefinition = "TEXT")
    private String conflictDescription;

    @Column(name = "related_ids", length = 200)
    private String relatedIds;

    @Column(name = "conflict_date", nullable = false)
    private LocalDate conflictDate;

    @Column(name = "time_slot_id")
    private Long timeSlotId;

    @Column(name = "status")
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
