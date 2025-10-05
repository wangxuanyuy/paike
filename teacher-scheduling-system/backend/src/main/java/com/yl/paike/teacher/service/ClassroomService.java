package com.yl.paike.teacher.service;

import com.yl.paike.teacher.dto.ClassroomCreateDTO;
import com.yl.paike.teacher.dto.ClassroomDTO;

import java.util.List;

public interface ClassroomService {
    
    ClassroomDTO createClassroom(ClassroomCreateDTO createDTO);
    
    ClassroomDTO getClassroomById(Long id);
    
    List<ClassroomDTO> getAllActiveClassrooms();
    
    ClassroomDTO updateClassroom(Long id, ClassroomCreateDTO updateDTO);
    
    void deleteClassroom(Long id);
}
