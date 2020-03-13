package com.liurui.redis.demo8;

import com.liurui.redis.demo8.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liu-rui
 * @date 2019-08-15 10:05
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class AppTests {
    @Autowired
    UserService userService;

    @Test
    public void test() {
        System.out.println(userService.add(1));
    }
}
