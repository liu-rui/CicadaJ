package com.liurui.rabbitmq.delay.producer;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageBuilderSupport;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author liu-rui
 * @date 2019-08-26 9:34
 * @description 延迟消息发送者
 */
public class DelayMessageSenderImpl implements DelayMessageSender {
    private final RabbitTemplate delayMessageTemplate;


    public DelayMessageSenderImpl(RabbitTemplate delayMessageTemplate) {
        this.delayMessageTemplate = delayMessageTemplate;
    }

    /**
     * 发送延迟消息
     *
     * @param executeMillis 触发时间 时间戳
     * @param data          消息内容
     */
    @Override
    public void send(long executeMillis, Object data) {
        send(executeMillis, JSON.toJSONBytes(data), null);
    }

    /**
     * 发送数据
     *
     * @param executeMillis 触发时间(时间戳)
     * @param data          消息内容
     * @param retry         尝试第几次
     */
    @Override
    public void send(long executeMillis, byte[] data, Integer retry) {
        Assert.notNull(data, "data is null");
        final MessageBuilderSupport<Message> builder = MessageBuilder.withBody(data)
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setHeader(HEADER_TTL, executeMillis);

        if (Objects.nonNull(retry)) {
            builder.setHeader(HEADER_RETRY, retry);
        }
        final Message message = builder.build();

        send(message);
    }

    @Override
    public void send(Message message) {
        long executeMillis = 0;
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        if (!CollectionUtils.isEmpty(headers)) {
            Object value = headers.get(HEADER_TTL);

            if (value != null) {
                executeMillis = (long) value;
            }
        }
        long duration = executeMillis - System.currentTimeMillis();
        //判断当前时间适合使用哪个队列
        String routerKey = getRouterKey(duration);

        delayMessageTemplate.send(routerKey, message);
    }

    @Override
    public boolean expired(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        if (!CollectionUtils.isEmpty(headers) && headers.containsKey(HEADER_TTL)) {
            long executeMillis = (long) headers.get(HEADER_TTL);

            return executeMillis <= System.currentTimeMillis();
        }
        return true;
    }

    /**
     * 通过消息获取当前是第几次重试
     *
     * @param message 消息
     * @return 第几次重试
     */
    @Override
    public int getRetry(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        if (!CollectionUtils.isEmpty(headers) && headers.containsKey(HEADER_RETRY)) {
            return (int) headers.get(HEADER_RETRY);
        }
        return 0;
    }

    /**
     * 根据时间选择队列
     *
     * @param duration
     * @return 返回符合时间段内的队列实例
     */
    private String getRouterKey(long duration) {
        if (duration <= 0) {
            return "master";
        }
        long days = TimeUnit.MILLISECONDS.toDays(duration);

        if (days >= 2) {
            return "2d";
        }

        if (days >= 1) {
            return "1d";
        }
        long hours = TimeUnit.MILLISECONDS.toHours(duration);

        if (hours >= 5) {
            return "5h";
        }
        if (hours >= 1) {
            return "1h";
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);

        if (minutes >= 15) {
            return "15m";
        }
        if (minutes >= 5) {
            return "5m";
        }

        if (minutes >= 1) {
            return "1m";
        }
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);

        if (seconds >= 15) {
            return "15s";
        }
        if (seconds >= 5) {
            return "5s";
        }
        if (seconds >= 2) {
            return "2s";
        }

        return "1s";
    }
}
