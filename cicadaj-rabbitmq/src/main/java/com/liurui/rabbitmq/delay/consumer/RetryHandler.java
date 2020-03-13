package com.liurui.rabbitmq.delay.consumer;

import org.springframework.amqp.core.Message;

/**
 * 重试次数处理类
 *
 * @author 刘锐
 * @date 2019-1-21 14:34:16
 */
public interface RetryHandler {
    RetryHandler DEFAULT = message -> false;

    /**
     * 处理重试次数逻辑
     *
     * @param message 消息体（包含配置和消息）
     * @return 是否重试成功，不需要再重试
     */
    boolean exec(Message message);
}
