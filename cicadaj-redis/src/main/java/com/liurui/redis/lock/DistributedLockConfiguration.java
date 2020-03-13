package com.liurui.redis.lock;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu-rui
 * @date 2019-08-15 9:34
 * @description
 */
@EnableConfigurationProperties(DistributedLockProperties.class)
@Configuration
public class DistributedLockConfiguration {
    @Bean
    DistributedLockBuilder lockBuilder() {
        return new DistributedLockBuilderImpl();
    }

    @Bean
    public DistributedLockAdvice lockAdvice() {
        return new DistributedLockAdvice();
    }
}
