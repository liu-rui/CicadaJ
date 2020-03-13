package com.liurui.rabbitmq;

import com.liurui.rabbitmq.api.log.ApiLogConfiguration;
import com.liurui.rabbitmq.limit.LimitConfiguration;
import com.liurui.rabbitmq.log.LogConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: 刘锐
 * @date: 19-06-24 下午19:21
 * @description: api自动配置类
 */
@Configuration
@Import({LogConfiguration.class,
        ApiLogConfiguration.class,
        LimitConfiguration.class})
public class ApiAutoConfiguration {
}
