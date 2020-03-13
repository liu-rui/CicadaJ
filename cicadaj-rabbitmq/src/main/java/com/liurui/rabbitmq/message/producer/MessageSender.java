package com.liurui.rabbitmq.message.producer;

/**
 * @author liu-rui
 * @date 2019-08-19 16:41
 * @description
 */
public interface MessageSender {
    void send(String routingKey, Object message);
}
