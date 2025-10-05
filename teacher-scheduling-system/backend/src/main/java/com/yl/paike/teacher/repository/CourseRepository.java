package com.yl.paike.teacher.repository;

import com.yl.paike.teacher.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;  // 添加这个导入
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>,
        JpaSpecificationExecutor<Course> {  // 添加这个接口

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByIsActiveTrue();

    List<Course> findByAgeGroupAndIsActiveTrue(Integer ageGroup);

    List<Course> findByCourseNameContaining(String name);
}