package com.yl.paike.teacher.service.impl;

import com.yl.paike.teacher.dto.*;
import com.yl.paike.teacher.entity.Teacher;
import com.yl.paike.teacher.exception.BusinessException;
import com.yl.paike.teacher.exception.EntityNotFoundException;
import com.yl.paike.teacher.repository.TeacherAssignmentRepository;
import com.yl.paike.teacher.repository.TeacherRepository;
import com.yl.paike.teacher.service.TeacherService;
import com.yl.paike.teacher.util.BeanConverter;
import com.yl.paike.teacher.util.Constants;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherAssignmentRepository assignmentRepository;

    @Override
    @Transactional
    public TeacherDTO createTeacher(TeacherCreateDTO createDTO) {
        // 检查手机号是否已存在
        teacherRepository.findByPhone(createDTO.getPhone()).ifPresent(t -> {
            throw new BusinessException("该手机号已被注册");
        });

        Teacher teacher = new Teacher();
        teacher.setTeacherName(createDTO.getTeacherName());
        teacher.setPhone(createDTO.getPhone());
        teacher.setEmail(createDTO.getEmail());
        teacher.setSpecialties(createDTO.getSpecialties());
        teacher.setAgeGroupList(createDTO.getAgeGroups());
        teacher.setIsActive(true);

        Teacher savedTeacher = teacherRepository.save(teacher);
        log.info("教师创建成功，教师编号：{}", savedTeacher.getTeacherCode());

        return convertToDTO(savedTeacher);
    }

    @Override
    @Cacheable(value = "teachers", key = "#id")
    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("教师", id));
        return convertToDTO(teacher);
    }

    @Override
    public Page<TeacherDTO> getTeacherList(String keyword, List<Integer> ageGroups,
                                           Boolean isActive, Pageable pageable) {
        Specification<Teacher> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword + "%";
                Predicate namePredicate = cb.like(root.get("teacherName"), likePattern);
                Predicate phonePredicate = cb.like(root.get("phone"), likePattern);
                predicates.add(cb.or(namePredicate, phonePredicate));
            }

            if (ageGroups != null && !ageGroups.isEmpty()) {
                List<Predicate> ageGroupPredicates = new ArrayList<>();
                for (Integer ageGroup : ageGroups) {
                    ageGroupPredicates.add(cb.like(root.get("ageGroups"), "%" + ageGroup + "%"));
                }
                predicates.add(cb.or(ageGroupPredicates.toArray(new Predicate[0])));
            }

            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Teacher> teacherPage = teacherRepository.findAll(spec, pageable);
        return teacherPage.map(this::convertToDTO);
    }

    @Override
    @Transactional
    @CacheEvict(value = "teachers", key = "#id")
    public TeacherDTO updateTeacher(Long id, TeacherUpdateDTO updateDTO) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("教师", id));

        if (updateDTO.getTeacherName() != null) {
            teacher.setTeacherName(updateDTO.getTeacherName());
        }

        if (updateDTO.getPhone() != null) {
            // 检查新手机号是否被其他教师使用
            teacherRepository.findByPhone(updateDTO.getPhone()).ifPresent(existingTeacher -> {
                if (!existingTeacher.getId().equals(id)) {
                    throw new BusinessException("该手机号已被其他教师使用");
                }
            });
            teacher.setPhone(updateDTO.getPhone());
        }

        if (updateDTO.getEmail() != null) {
            teacher.setEmail(updateDTO.getEmail());
        }

        if (updateDTO.getSpecialties() != null) {
            teacher.setSpecialties(updateDTO.getSpecialties());
        }

        if (updateDTO.getAgeGroups() != null) {
            teacher.setAgeGroupList(updateDTO.getAgeGroups());
        }

        Teacher savedTeacher = teacherRepository.save(teacher);
        log.info("教师信息更新成功，教师ID：{}", id);

        return convertToDTO(savedTeacher);
    }

    @Override
    @Transactional
    @CacheEvict(value = "teachers", key = "#id")
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("教师", id));

        // 检查教师是否有未完成的课程安排
        LocalDate today = LocalDate.now();
        List<com.yl.paike.teacher.entity.TeacherAssignment> futureAssignments =
                assignmentRepository.findByTeacherIdAndDateRange(id, today, today.plusMonths(3));

        if (!futureAssignments.isEmpty()) {
            throw new BusinessException("教师有未完成的课程安排，无法删除");
        }

        teacher.setIsActive(false);
        teacherRepository.save(teacher);

        log.info("教师删除成功，教师ID：{}", id);
    }

    @Override
    public List<TeacherDTO> getAvailableTeachers(Integer ageGroup, LocalDate date, Long timeSlotId) {
        // 获取符合年龄组要求的活跃教师
        List<Teacher> eligibleTeachers = teacherRepository.findByAgeGroupAndActive(String.valueOf(ageGroup));

        List<TeacherDTO> availableTeachers = new ArrayList<>();

        for (Teacher teacher : eligibleTeachers) {
            // 检查教师在指定时间是否有冲突
            List<com.yl.paike.teacher.entity.TeacherAssignment> conflicts =
                    assignmentRepository.findConflictingAssignments(teacher.getId(), date, timeSlotId);

            if (conflicts.isEmpty()) {
                availableTeachers.add(convertToDTO(teacher));
            }
        }

        return availableTeachers;
    }

    /**
     * 实体转DTO
     */
    private TeacherDTO convertToDTO(Teacher teacher) {
        TeacherDTO dto = BeanConverter.convert(teacher, TeacherDTO.class);
        dto.setAgeGroups(teacher.getAgeGroupList());
        return dto;
    }
}