package com.liurui.rabbitmq.web.formatter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liu-rui
 * @date 2019-07-12 9:52
 * @description
 */
public class DateConverter implements Converter<String, Date> {
    public static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public Date convert(String s) {
        try {
            return DATE_FORMAT.get().parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException("不是有效的时间格式,需要的格式是yyyy-MM-dd HH:mm:ss；传递的值为:" + s);
        }
    }
}
