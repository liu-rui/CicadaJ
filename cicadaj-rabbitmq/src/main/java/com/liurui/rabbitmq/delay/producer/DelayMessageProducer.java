package com.liurui.rabbitmq.delay.producer;

import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author liu-rui
 * @date 2019-08-26 9:31
 * @description
 */
public interface DelayMessageProducer {
    /**
     * 发送数据
     *
     * @param key           key
     * @param executeMillis 触发时间(时间戳)
     * @param data          消息内容
     */
    void send(String key, long executeMillis, Object data);

    /**
     * 发送数据
     *
     * @param key         key
     * @param executeDate 触发时间
     * @param data        消息内容
     */
    default void send(String key, Date executeDate, Object data) {
        Assert.notNull(executeDate, "executeDate is null");
        send(key, executeDate.getTime(), data);
    }

    /**
     * 发送数据
     *
     * @param key         key
     * @param executeDate 触发时间
     * @param data        消息内容
     */
    default void send(String key, LocalDateTime executeDate, Object data) {
        Assert.notNull(executeDate, "executeDate is null");
        long executeMillis = executeDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        send(key, executeMillis, data);
    }

    /**
     * 在当前时间多久后发送消息
     *
     * @param key      key
     * @param duration 时间间隔
     * @param data     消息内容
     */
    default void sendAfterNow(String key, Duration duration, Object data) {
        Assert.notNull(duration, "duration is null");
        LocalDateTime executeDate = LocalDateTime.now().plus(duration);

        send(key, executeDate, data);
    }
}
