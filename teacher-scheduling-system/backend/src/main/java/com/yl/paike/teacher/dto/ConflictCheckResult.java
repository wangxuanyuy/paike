package com.yl.paike.teacher.dto;

import lombok.Data;

@Data
public class ConflictCheckResult {
    private Boolean hasConflicts;
    private String conflictDescription;
    
    public ConflictCheckResult() {
        this.hasConflicts = false;
        this.conflictDescription = "";
    }
}
