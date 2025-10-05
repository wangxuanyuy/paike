package com.yl.paike.teacher.repository;

import com.yl.paike.teacher.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    
    Optional<Classroom> findByClassroomCode(String classroomCode);
    
    List<Classroom> findByIsActiveTrue();
    
    List<Classroom> findByClassroomNameContaining(String name);
}
