package com.liurui.redis.demo2;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.liurui.redis.demo2.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("demo2")
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "")
@SpringBootApplication
public class AppTests {
    @Autowired
    Environment environment;
    @Autowired
    UserService userService;

    @Test
    public void testGet() {
        System.out.println(userService.get(1));
        System.out.println(userService.get(1));
        System.out.println(userService.get(1));
    }

    /**
     * 使用3个线程同时调用get方法；
     * 结论：
     * 当缓存不存在时，调用load会有多次调用；无法保证同步
     *
     * @throws InterruptedException
     */
    @Test
    public void testGetConcurrent() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> System.out.println(userService.get(10)));
        executorService.execute(() -> System.out.println(userService.get(10)));
        executorService.execute(() -> System.out.println(userService.get(10)));

        while (!executorService.isTerminated()) {
            Thread.sleep(10);
        }
    }


    /**
     * 使用3个线程同时调用get方法；
     * 结论：
     * 当缓存不存在时，调用load会有多次调用；
     * 为了确保只有线程load数据，需要使用注解CachePenetrationProtect
     *
     * @throws InterruptedException
     */
    @Test
    public void testGetUsingAnnotationConcurrent() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> System.out.println(userService.getUsingAnnotation(10)));
        executorService.execute(() -> System.out.println(userService.getUsingAnnotation(10)));
        executorService.execute(() -> System.out.println(userService.getUsingAnnotation(10)));

        while (!executorService.isTerminated()) {
            Thread.sleep(10);
        }
    }

    @Test
    public void testLock() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> userService.lock());
        executorService.execute(() -> userService.lock());
        executorService.execute(() -> userService.lock());

        while (!executorService.isTerminated()) {
            Thread.sleep(10);
        }
    }

    @Test
    public void getUsingAnnotationBoth() throws InterruptedException {
        /**
         * REDIS: GET,PSETEX
         * 原理：
         *    首先访问本地缓存，本地缓存不存在调用redis的GET获取远程缓存
         *    远程缓存不存在，执行业务逻辑
         *    然后将结果写入远程缓存中，调用redis的PSETEX
         */
        System.out.println(userService.getUsingAnnotationBoth(1));
        System.out.println(userService.getUsingAnnotationBoth(1));
        System.out.println(userService.getUsingAnnotationBoth(1));

        /**
         * REDIS GET
         * 由于本地缓存已经过期了，调用redis的GET方法获取数据
         * 远程存在数据，写入本地
         */
        Thread.sleep(6000);
        System.out.println("------------------");
        System.out.println(userService.getUsingAnnotationBoth(1));
        System.out.println(userService.getUsingAnnotationBoth(1));
        System.out.println(userService.getUsingAnnotationBoth(1));


        /**
         * REDIS: GET,PSETEX
         * 本地缓存已经过期，调用远程缓存
         * 远程环境也过期了
         * 调用业务逻辑
         * 保存到远程的REDIS中
         * 保存到本地的缓存中
         */
        Thread.sleep(15000);
        System.out.println("------------------");
        System.out.println(userService.getUsingAnnotationBoth(1));
        System.out.println(userService.getUsingAnnotationBoth(1));
        System.out.println(userService.getUsingAnnotationBoth(1));
    }
}
