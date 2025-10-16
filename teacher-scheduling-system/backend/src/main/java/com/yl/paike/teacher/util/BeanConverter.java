package com.yl.paike.teacher.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanConverter {
    
    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象转换失败", e);
        }
    }
    
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }
        return sourceList.stream()
                .map(source -> convert(source, targetClass))
                .collect(Collectors.toList());
    }
}
