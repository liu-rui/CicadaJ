package com.liurui.rabbitmq.web.formatter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author liu-rui
 * @date 2019-07-12 9:52
 * @description
 */
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime convert(String s) {
        return LocalDateTime.parse(s, DATE_TIME_FORMATTER);
    }
}