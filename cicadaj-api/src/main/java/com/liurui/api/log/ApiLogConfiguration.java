package com.liurui.api.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 刘锐
 * @date: 19-06-24 下午19:21
 * @description: 日志配置类
 */
@Configuration
@EnableConfigurationProperties(ApiLogProperties.class)
@ConditionalOnProperty(name = "cicadaj.api.log.enabled", matchIfMissing = true)
public class ApiLogConfiguration {
    @Bean
    public ApiLogPointcut logPointcut() {
        return new ApiLogPointcut();
    }

    @Bean
    public ApiLogAdvice logInterceptor() {
        return new ApiLogAdvice();
    }

    @Bean
    public ApiLogAdvisor logAdvisor() {
        return new ApiLogAdvisor();
    }
}
