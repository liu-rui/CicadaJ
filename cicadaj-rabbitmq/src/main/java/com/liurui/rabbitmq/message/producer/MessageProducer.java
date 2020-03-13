package com.liurui.rabbitmq.message.producer;

/**
 * @author liu-rui
 * @date 2019-08-27 16:46
 * @description
 */
public interface MessageProducer {
    void send(String key, Object data);

    void send(String key, String routingKey, Object data);
}
