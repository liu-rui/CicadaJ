package com.liurui.rabbitmq.delay.producer;

import org.springframework.amqp.core.Message;

/**
 * rabbitmq 消息生产者
 *
 * @author 刘锐
 * @date 2018-11-19 11:10:55
 */
public interface DelayMessageSender {
    String HEADER_TTL = "d_ttl";
    String HEADER_RETRY = "d_retry";

    /**
     * 发送数据
     *
     * @param executeMillis 触发时间(时间戳)
     * @param data          消息内容
     */
    void send(long executeMillis, Object data);

    /**
     * 发送数据
     *
     * @param executeMillis 触发时间(时间戳)
     * @param data          消息内容
     * @param retry         尝试第几次
     */
    void send(long executeMillis, byte[] data, Integer retry);

    /**
     * 发送消息
     *
     * @param message 消息体
     */
    void send(Message message);

    /**
     * 是否到了执行时间了
     *
     * @param message 消息
     * @return
     */
    boolean expired(Message message);


    /**
     * 通过消息获取当前是第几次重试
     *
     * @param message 消息
     * @return 第几次重试
     */
    int getRetry(Message message);
}
