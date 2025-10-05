package com.yl.paike.teacher.repository;

import com.yl.paike.teacher.entity.ConflictWarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConflictWarningRepository extends JpaRepository<ConflictWarning, Long>, JpaSpecificationExecutor<ConflictWarning> {
    
    List<ConflictWarning> findByConflictType(Integer conflictType);
    
    List<ConflictWarning> findByStatus(Integer status);
    
    List<ConflictWarning> findByConflictDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT cw FROM ConflictWarning cw WHERE cw.conflictDate BETWEEN :startDate AND :endDate AND cw.status = 1 ORDER BY cw.createdAt DESC")
    List<ConflictWarning> findUnresolvedConflicts(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Modifying
    @Query("DELETE FROM ConflictWarning cw WHERE cw.status = :status AND cw.createdAt < :beforeDate")
    void deleteByStatusAndCreatedAtBefore(@Param("status") Integer status, @Param("beforeDate") LocalDateTime beforeDate);
}
