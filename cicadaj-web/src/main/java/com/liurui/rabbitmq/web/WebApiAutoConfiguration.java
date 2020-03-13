package com.liurui.rabbitmq.web;


import com.liurui.rabbitmq.web.controller.HealthController;
import com.liurui.rabbitmq.web.formatter.FormatterWebMvcConfigurer;
import com.liurui.rabbitmq.web.formatter.JacksonConfiguraiton;
import com.liurui.rabbitmq.web.log.WebLogConfiguration;
import com.liurui.rabbitmq.web.swagger.SwaggerConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: 刘锐
 * @date: 18-12-24 下午7:30
 * @description: webApi项目自动配置
 */
@Configuration
@Import(value = {WebLogConfiguration.class,
        SwaggerConfiguration.class,
        CorsConfig.class,
        FormatterWebMvcConfigurer.class,
        JacksonConfiguraiton.class})
@ComponentScan(basePackageClasses = {HealthController.class})
public class WebApiAutoConfiguration {
}