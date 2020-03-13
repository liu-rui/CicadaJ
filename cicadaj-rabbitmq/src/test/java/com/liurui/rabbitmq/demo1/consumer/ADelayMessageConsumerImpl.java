package com.liurui.rabbitmq.demo1.consumer;

import com.liurui.rabbitmq.delay.consumer.DelayMessageConsumer;
import org.springframework.stereotype.Component;

@Component("aDelayMessageConsumer")
public class ADelayMessageConsumerImpl implements DelayMessageConsumer<String> {

    @Override
    public boolean onMessage(String message) {
        System.out.println("A消费者，消息为:" + message);
        return false;
    }
}
