package com.liurui.redis.demo4;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.liurui.redis.demo4.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "")
@ActiveProfiles("demo4")
public class AppTests {
    @Autowired
    UserService userService;

    @Test
    public void test() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> userService.lock());
        executorService.execute(() -> userService.lock());
        executorService.execute(() -> userService.lock());

        while (!executorService.isTerminated()) {
            Thread.sleep(10);
        }
    }
}
