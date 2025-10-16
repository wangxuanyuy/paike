package com.yl.paike.teacher.repository;

import com.yl.paike.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    
    Optional<Teacher> findByTeacherCode(String teacherCode);
    
    Optional<Teacher> findByPhone(String phone);
    
    List<Teacher> findByIsActiveTrue();
    
    @Query("SELECT t FROM Teacher t WHERE t.isActive = true AND t.ageGroups LIKE %:ageGroup%")
    List<Teacher> findByAgeGroupAndActive(@Param("ageGroup") String ageGroup);
    
    @Query("SELECT t FROM Teacher t WHERE t.teacherName LIKE %:keyword% OR t.phone LIKE %:keyword%")
    List<Teacher> searchByKeyword(@Param("keyword") String keyword);
}
