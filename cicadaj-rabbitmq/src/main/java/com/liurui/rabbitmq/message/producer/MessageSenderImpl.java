package com.liurui.rabbitmq.message.producer;

import com.alibaba.fastjson.JSON;
import com.liurui.rabbitmq.message.config.MessageItemProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;

/**
 * @author liu-rui
 * @date 2019-08-19 17:14
 * @description
 */
public class MessageSenderImpl implements MessageSender {
    private MessageItemProperties messageItemProperties;
    private RabbitTemplate rabbitTemplate;

    public MessageSenderImpl(MessageItemProperties messageItemProperties, RabbitTemplate rabbitTemplate) {
        this.messageItemProperties = messageItemProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(String routingKey, Object message) {
        Assert.notNull(routingKey, "routingKey is not null");
        Assert.notNull(message, "message is not null");
        Message msg = MessageBuilder.withBody(JSON.toJSONBytes(message))
                .setDeliveryMode(messageItemProperties.isDurable() ? MessageDeliveryMode.PERSISTENT : MessageDeliveryMode.NON_PERSISTENT)
                .build();
        rabbitTemplate.send(routingKey, msg);
    }
}
