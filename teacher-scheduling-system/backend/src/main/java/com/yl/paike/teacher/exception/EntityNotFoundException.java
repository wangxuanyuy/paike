package com.yl.paike.teacher.exception;

public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("%s (ID: %d) 不存在", entityName, id));
    }
}
