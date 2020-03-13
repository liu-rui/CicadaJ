package com.liurui.rabbitmq.web.formatter;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author liu-rui
 * @date 2019-07-12 10:14
 * @description
 */
public class LocalDateTimeConverterTest {
    private LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

    @Test
    public void convert() {
        Assert.assertEquals(LocalDateTime.of(2019, 10, 11, 15, 3, 12), localDateTimeConverter.convert("2019-10-11 15:03:12"));
    }
}
