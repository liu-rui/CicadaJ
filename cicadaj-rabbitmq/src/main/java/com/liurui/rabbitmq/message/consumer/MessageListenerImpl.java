package com.liurui.rabbitmq.message.consumer;

import com.alibaba.fastjson.JSON;
import com.liurui.rabbitmq.FailedMessageLogByFile;
import com.liurui.rabbitmq.message.config.MessageItemProperties;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author liu-rui
 * @date 2019-08-19 18:16
 * @description
 */
@Slf4j
public class MessageListenerImpl implements ChannelAwareMessageListener {
    private String messageConsumerName;
    private FailedMessageLogByFile failedMessageLogByFile;
    private MessageItemProperties properties;
    private MessageConsumer messageConsumer;
    private Type argumentType;

    public MessageListenerImpl(String messageConsumerName, FailedMessageLogByFile failedMessageLogByFile, MessageItemProperties properties, MessageConsumer<?> messageConsumer) {
        this.messageConsumerName = messageConsumerName;
        this.failedMessageLogByFile = failedMessageLogByFile;
        this.properties = properties;
        this.messageConsumer = messageConsumer;
        final Type[] interfaces = messageConsumer.getClass().getGenericInterfaces();
        final ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
        argumentType = parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long start = System.currentTimeMillis();
        boolean ret;
        try {
            final Object data = JSON.parseObject(message.getBody(), argumentType);
            ret = messageConsumer.onMessage(data);

            if (log.isInfoEnabled()) {
                log.info("消费结束 队列名称:{} 内容:{} 结果:{} 耗时:{}毫秒",
                        properties.getQueue(),
                        new String(message.getBody()),
                        ret ? "成功" : "失败",
                        System.currentTimeMillis() - start);
            }
        } catch (Exception ex) {
            if (log.isErrorEnabled()) {
                log.error(String.format("消息消费时出现异常, 队列名称:%s 内容为:%s 耗时:%s毫秒；原因:",
                        properties.getQueue(),
                        new String(message.getBody()),
                        System.currentTimeMillis() - start), ex);
            }
            ret = false;
        }

        if (!ret && StringUtils.isNotBlank(properties.getFailedSavePath())) {
            failedMessageLogByFile.log(properties.getFailedSavePath(), String.format("队列:%s 内容:%s",
                    properties.getQueue(), new String(message.getBody())));
        }

        if (properties.isManualAck()) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
