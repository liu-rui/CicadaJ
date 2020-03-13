package com.liurui.rabbitmq;

import com.liurui.rabbitmq.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppTest.A.class})
@ActiveProfiles("dev")
public class AppTest {
    @Autowired
    CustomerService customerService;


    @Test
    public void test() {
        customerService.get();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SpringBootApplication
    public static class A {
    }
}
