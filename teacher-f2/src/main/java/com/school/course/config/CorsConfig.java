package com.school.course.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("=== CORS配置正在加载 ===");

        registry.addMapping("/**")
                // 使用 allowedOriginPatterns 替代 allowedOrigins
                .allowedOriginPatterns("*")  // 这里改为 allowedOriginPatterns
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)  // 保持为 true
                .maxAge(3600);

        System.out.println("=== CORS配置加载完成 ===");
    }
}