package com.yl.paike.teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class TeacherApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TeacherApplication.class, args);
        System.out.println("===========================================");
        System.out.println("教师排课系统启动成功！");
        System.out.println("访问地址: http://localhost:45082/api");
        System.out.println("===========================================");
    }
}
