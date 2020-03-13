package com.liurui.rabbitmq.demo2;

import com.alibaba.fastjson.JSON;
import com.liurui.rabbitmq.demo1.User;
import com.liurui.rabbitmq.message.consumer.MessageConsumer;
import org.springframework.stereotype.Component;

/**
 * @author liu-rui
 * @date 2019-08-26 11:44
 * @description
 */
@Component("bMessageConsumer")
public class BMessageConsumer implements MessageConsumer<User> {
    @Override
    public boolean onMessage(User message) {
        System.out.println("b消费者，消息：" + JSON.toJSONString(message));
        return false;
    }
}
