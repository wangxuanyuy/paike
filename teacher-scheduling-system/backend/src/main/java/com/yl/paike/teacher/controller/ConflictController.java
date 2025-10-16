package com.yl.paike.teacher.controller;

import com.yl.paike.teacher.dto.ConflictWarningDTO;
import com.yl.paike.teacher.dto.PageResult;
import com.yl.paike.teacher.dto.Result;
import com.yl.paike.teacher.service.ConflictDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/conflicts")
@RequiredArgsConstructor
@Slf4j
public class ConflictController {
    
    private final ConflictDetectionService conflictDetectionService;
    
    @GetMapping("/warnings")
    public Result<PageResult<ConflictWarningDTO>> getConflictWarnings(
            @RequestParam(required = false) Integer conflictType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ConflictWarningDTO> warningPage = conflictDetectionService.getConflictWarnings(
            conflictType, status, startDate, endDate, pageable);
        
        PageResult<ConflictWarningDTO> pageResult = new PageResult<>();
        pageResult.setContent(warningPage.getContent());
        pageResult.setTotalElements(warningPage.getTotalElements());
        pageResult.setTotalPages(warningPage.getTotalPages());
        pageResult.setPage(page);
        pageResult.setSize(size);
        
        return Result.success(pageResult);
    }
    
    @PutMapping("/warnings/{id}/resolve")
    public Result<Void> resolveConflictWarning(
            @PathVariable Long id,
            @RequestParam Integer resolution,
            @RequestParam(required = false) String remark) {
        log.info("处理冲突警告: ID={}, 处理结果={}", id, resolution);
        conflictDetectionService.resolveConflictWarning(id, resolution, remark);
        return Result.success(null, "处理成功");
    }
    
    @PostMapping("/check")
    public Result<Void> performConflictCheck(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("执行冲突检测: {} 至 {}", startDate, endDate);
        conflictDetectionService.performFullConflictCheck(startDate, endDate);
        return Result.success(null, "冲突检测完成");
    }
}
