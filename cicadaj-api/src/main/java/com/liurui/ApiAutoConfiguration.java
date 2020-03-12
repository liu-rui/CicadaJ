package com.liurui;

import com.liurui.limit.LimitConfiguration;
import com.liurui.log.LogConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.liurui.api.log.ApiLogConfiguration;

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
