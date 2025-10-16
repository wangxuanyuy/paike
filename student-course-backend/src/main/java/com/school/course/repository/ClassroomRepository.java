package com.school.course.repository;

import com.school.course.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findByIsActiveTrueOrderByClassroomName();
    Optional<Classroom> findByClassroomCode(String classroomCode);
}
