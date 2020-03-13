package com.liurui.redis.demo9;

import com.liurui.redis.lock.DistributedLockBuilder;
import com.liurui.redis.lock.DistributedLockable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liu-rui
 * @date 2019-08-15 14:58
 * @description
 */
@SpringBootApplication
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {
    @Autowired
    DistributedLockBuilder lockBuilder;


    @Test
    public void test() throws Exception {
        try (DistributedLockable lockable = lockBuilder.build("33",10)) {
            if (!lockable.lock()) {
                return;
            }
            Thread.sleep(40000);
        }
    }
}
