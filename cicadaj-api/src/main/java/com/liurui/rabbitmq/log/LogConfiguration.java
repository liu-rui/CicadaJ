package com.liurui.rabbitmq.log;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 刘锐
 * @date: 19-06-24 下午19:21
 * @description: 日志配置类
 */
@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class LogConfiguration {
    @Bean
    public StringAdapter stringAdapter() {
        return new StringAdapterImpl();
    }
}
