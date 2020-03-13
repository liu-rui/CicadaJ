package com.liurui.rabbitmq.message.producer;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * @author liu-rui
 * @date 2019-08-27 16:48
 * @description
 */
@Slf4j
public class MessageProducerImpl implements MessageProducer {
    private Map<String, MessageSender> messageSenderMap = Maps.newHashMap();

    @Override
    public void send(String key, Object data) {
        send(key, "", data);
    }

    @Override
    public void send(String key, String routingKey, Object data) {
        Assert.notNull(key, "key is null");
        Assert.notNull(routingKey, "routingKey is not null");
        Assert.notNull(data, "data is null");
        final MessageSender messageSender = messageSenderMap.get(key);

        if (Objects.isNull(messageSender)) {
            throw new IllegalArgumentException("key无效，找不到对应的消息配置");
        }

        if (log.isInfoEnabled()) {
            log.info("发送消息 key:{} 内容:{}", key, data);
        }
        messageSender.send(routingKey, data);
    }

    public void add(String key, MessageSender messageSender) {
        Assert.notNull(key, "key is null");
        Assert.notNull(messageSender, "MessageSender is null");
        messageSenderMap.put(key, messageSender);
    }
}
