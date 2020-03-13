package com.liurui.rabbitmq.message.consumer;

/**
 * @author liu-rui
 * @date 2019-08-19 15:09
 * @description 消息消费监听接口
 */
public interface MessageConsumer<T> {
    /**
     * 接收消息
     *
     * @param message 消息内容
     * @return 成功返回true，失败返回false
     */
    boolean onMessage(T message);
}
