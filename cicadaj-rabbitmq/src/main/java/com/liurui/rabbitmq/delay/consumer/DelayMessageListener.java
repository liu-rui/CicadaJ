package com.liurui.rabbitmq.delay.consumer;

import com.alibaba.fastjson.JSON;
import com.liurui.rabbitmq.delay.config.DelayMessageItemProperties;
import com.liurui.rabbitmq.delay.producer.DelayMessageSender;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 消费者类
 *
 * @author 刘锐
 * @date 2018-11-19 11:09:40
 */
@Slf4j
public class DelayMessageListener implements ChannelAwareMessageListener {
    private String delayMessageConsumerName;
    private Queue masterQueue;
    private DelayMessageSender delayMessageSender;
    private DelayMessageConsumer delayMessageConsumer;
    private RetryHandler retryHandler;
    private Type argumentType;
    private DelayMessageItemProperties delayMessageItemProperties;

    public DelayMessageListener(String delayMessageConsumerName,
                                Queue masterQueue,
                                DelayMessageSender delayMessageSender,
                                DelayMessageConsumer delayMessageConsumer,
                                RetryHandler retryHandler, DelayMessageItemProperties delayMessageItemProperties) {
        this.delayMessageConsumerName = delayMessageConsumerName;
        this.masterQueue = masterQueue;
        this.delayMessageSender = delayMessageSender;
        this.delayMessageConsumer = delayMessageConsumer;
        this.retryHandler = retryHandler;
        this.delayMessageItemProperties = delayMessageItemProperties;
        final Type[] interfaces = delayMessageConsumer.getClass().getGenericInterfaces();
        final ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
        argumentType = parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long start = System.currentTimeMillis();
        try {
            if (!delayMessageSender.expired(message)) {
                delayMessageSender.send(message);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            final Object content = JSON.parseObject(message.getBody(), argumentType);
            boolean result = delayMessageConsumer.onMessage(content);


            if (result) {
                if (log.isInfoEnabled()) {
                    log.info("延迟队列消费结束 队列名称:{} 内容:{} 结果:成功 耗时:{}毫秒",
                            masterQueue.getName(),
                            content,
                            System.currentTimeMillis() - start);
                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                String logMessage = String.format("延迟队列消费结束 队列名称:%s 内容:%s 结果:失败 耗时:%s毫秒",
                        masterQueue.getName(),
                        content,
                        System.currentTimeMillis() - start);

                if (delayMessageItemProperties.isErrorLogLevelWhenRetryFailed()) {
                    if (log.isErrorEnabled()) {
                        log.error(logMessage);
                    }
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn(logMessage);
                    }
                }
                requeue(message, channel, System.currentTimeMillis() - start);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(String.format("延迟队列消费异常 队列名称:%s,内容:%s；耗时:%s毫秒；原因:",
                        masterQueue.getName(),
                        new String(message.getBody()),
                        System.currentTimeMillis() - start), e);
            }
            requeue(message, channel, System.currentTimeMillis() - start);
        }
    }

    /**
     * 判断是否需要重回队列
     *
     * @param message 消息结构体
     * @param channel channel
     * @param escaped 消耗的毫秒数
     * @throws IOException
     */
    private void requeue(Message message, Channel channel, long escaped) throws IOException {
        if (!retryHandler.exec(message)) {
            if (log.isErrorEnabled()) {
                log.error("延迟队列执行最终宣判为失败 队列名称:{} 内容:{} 耗时:{}毫秒",
                        masterQueue.getName(),
                        new String(message.getBody()),
                        escaped);
            }
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
