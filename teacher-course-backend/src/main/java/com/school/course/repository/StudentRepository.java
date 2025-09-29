package com.school.course.repository;

import com.school.course.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAgeGroup(Integer ageGroup);
    List<Student> findByRegistrationStatus(Integer status);
    Optional<Student> findByStudentCode(String studentCode);
    Optional<Student> findByParentPhone(String phone);
    
    @Query("SELECT s FROM Student s WHERE s.studentName LIKE %:name% OR s.parentName LIKE %:name%")
    List<Student> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT s FROM Student s WHERE s.isActive = true ORDER BY s.createdAt DESC")
    List<Student> findActiveStudents();
}
