package com.liurui.rabbitmq.api.log;

import java.lang.annotation.*;


/**
 * @author: 刘锐
 * @date: 19-06-24 下午19:21
 * @description: 日志注解，可以标注在类或特定方法上。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {
}
