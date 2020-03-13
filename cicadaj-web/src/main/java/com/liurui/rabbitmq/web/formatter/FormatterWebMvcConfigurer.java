package com.liurui.rabbitmq.web.formatter;

import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liu-rui
 * @date 2019-07-12 10:04
 * @description
 */
public class FormatterWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
        registry.addConverter(new LocalDateTimeConverter());
    }
}
