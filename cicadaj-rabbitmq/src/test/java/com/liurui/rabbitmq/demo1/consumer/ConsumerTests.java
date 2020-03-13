package com.liurui.rabbitmq.demo1.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liu-rui
 * @date 2019-08-19 9:58
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@Import({ADelayMessageConsumerImpl.class, BDelayMessageConsumerImpl.class})
@ActiveProfiles("demo1")
public class ConsumerTests {
    @Test
    public void test() throws InterruptedException {
        Thread.sleep(300000);
    }
}