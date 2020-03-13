package com.liurui.rabbitmq.demo2;

import com.liurui.rabbitmq.demo1.User;
import com.liurui.rabbitmq.message.producer.MessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liu-rui
 * @date 2019-08-19 15:20
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@ActiveProfiles("demo2")
@Import({AMessageConsumer.class, BMessageConsumer.class})
public class AppTests {
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    Environment environment;

    @Test
    public void test() throws InterruptedException {
        messageProducer.send("a", "消息A");
        messageProducer.send("b", "aa", new User(45, "b消息"));

        Thread.sleep(500000);
    }
}
