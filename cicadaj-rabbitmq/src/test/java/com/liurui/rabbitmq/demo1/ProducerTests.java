package com.liurui.rabbitmq.demo1;

import com.liurui.rabbitmq.delay.producer.DelayMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@ActiveProfiles("demo1")
public class ProducerTests {

    @Autowired
    private DelayMessageProducer delayMessageProducer;


    @Test
    public void test() throws InterruptedException {
        delayMessageProducer.sendAfterNow("a", Duration.ofSeconds(10), "A消息");
        delayMessageProducer.sendAfterNow("b", Duration.ofSeconds(10), new User(30, "hello world"));
    }
}
