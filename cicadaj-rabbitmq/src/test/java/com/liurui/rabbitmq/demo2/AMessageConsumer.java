package com.liurui.rabbitmq.demo2;

import com.liurui.rabbitmq.message.consumer.MessageConsumer;
import org.springframework.stereotype.Component;

/**
 * @author liu-rui
 * @date 2019-08-19 18:25
 * @description
 */
@Component("aMessageConsumer")
public class AMessageConsumer implements MessageConsumer<String> {

    @Override
    public boolean onMessage(String message) {
        System.out.println("消费者A，消息：" + message);
        return true;
    }
}
