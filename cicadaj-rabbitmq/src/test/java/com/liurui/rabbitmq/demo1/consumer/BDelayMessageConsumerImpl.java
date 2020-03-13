package com.liurui.rabbitmq.demo1.consumer;

import com.alibaba.fastjson.JSON;
import com.liurui.rabbitmq.delay.consumer.DelayMessageConsumer;
import com.liurui.rabbitmq.demo1.User;
import org.springframework.stereotype.Component;

/**
 * @author liu-rui
 * @date 2019-08-26 11:09
 * @description
 */
@Component("bDelayMessageConsumer")
public class BDelayMessageConsumerImpl implements DelayMessageConsumer<User> {
    @Override
    public boolean onMessage(User message) {
        System.out.println("B消费者，消息为:" + JSON.toJSONString(message));
        return false;
    }
}
