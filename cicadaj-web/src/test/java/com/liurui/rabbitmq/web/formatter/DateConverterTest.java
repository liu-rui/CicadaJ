package com.liurui.rabbitmq.web.formatter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author liu-rui
 * @date 2019-07-12 10:18
 * @description
 */
public class DateConverterTest {
    DateConverter dateConverter = new DateConverter();

    @Test
    public void convert() {
        Calendar instance = Calendar.getInstance();
        instance.set(2019, 9, 11, 15, 3, 12);
        instance.set(Calendar.MILLISECOND, 0);
        Date date = instance.getTime();


        Assert.assertTrue(date.equals(dateConverter.convert("2019-10-11 15:03:12")));
        Assert.assertTrue(date.equals(dateConverter.convert("2019-10-11 15:3:12")));
    }
}