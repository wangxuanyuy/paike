package com.yl.paike.teacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "L_TEACHERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_code", unique = true, nullable = false, length = 20)
    private String teacherCode;

    @Column(name = "teacher_name", nullable = false, length = 50)
    private String teacherName;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "specialties", columnDefinition = "TEXT")
    private String specialties;

    @Column(name = "age_groups", nullable = false, length = 20)
    private String ageGroups;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void generateTeacherCode() {
        if (this.teacherCode == null || this.teacherCode.isEmpty()) {
            this.teacherCode = "TEA" + System.currentTimeMillis();
        }
    }

    @Transient
    public List<Integer> getAgeGroupList() {
        if (this.ageGroups == null || this.ageGroups.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(this.ageGroups.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Transient
    public void setAgeGroupList(List<Integer> ageGroupList) {
        if (ageGroupList != null && !ageGroupList.isEmpty()) {
            this.ageGroups = ageGroupList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        } else {
            this.ageGroups = "";
        }
    }
}
