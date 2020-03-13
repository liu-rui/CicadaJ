package com.liurui.rabbitmq.web.log;

import com.liurui.rabbitmq.ReturnData;
import com.liurui.rabbitmq.log.StringConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author: 刘锐
 * @date: 18-12-24 下午7:30
 * @description: controller层日志配置
 */
@EnableConfigurationProperties(WebLogProperties.class)
@Configuration
public class WebLogConfiguration {
    /**
     * 创建controller层日志上下文对象
     *
     * @return controller层日志属性对象
     */
    @Bean
    public WebLogContext controllerLogContext() {
        return new DefaultWebLogContext();
    }

    @Bean
    @ConditionalOnProperty(name = "cicadaj.web.log.enabled", matchIfMissing = true)
    public WebLogAdvice controllerLog() {
        return new WebLogAdvice();
    }

    @Bean
    public StringConverter httpServletRequestStringConverter() {
        return object -> {
            if (object instanceof HttpServletRequest) {
                return ReturnData.success("HttpServletRequest");
            }
            return ReturnData.serverError();
        };
    }

    @Bean
    public StringConverter httpServletResponseStringConverter() {
        return object -> {
            if (object instanceof HttpServletResponse) {
                return ReturnData.success("HttpServletResponse");
            }
            return ReturnData.serverError();
        };
    }
}
