// JpaConfig.java
package com.school.course.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.school.course.repository")
@EnableTransactionManagement
public class JpaConfig {
    // JPA配置，Spring Boot 自动配置会处理大部分设置
}
