package com.liurui.rabbitmq.limit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author liu-rui
 * @date 2019/12/5 上午9:59
 * @description 限流器
 * @since 0.5.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Limiter {
    /**
     * 每秒的请求数
     *
     * @return 每秒的请求数
     */
    long qps();

    /**
     * 限制范围
     *
     * @return 范围
     */
    LimitScopeEnum scope() default LimitScopeEnum.STAND_ALONE;

    /**
     * 超时
     *
     * @return 超时
     */
    long timeout() default 0;


    /**
     * 超时时间单位
     *
     * @return 超时时间单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    /**
     * http状态码
     *
     * @return
     */
    int httpStatus() default 420;

    /**
     * 提示的消息
     *
     * @return 提示的消息
     */
    String message() default "排队人过多，请稍后再试";
}
