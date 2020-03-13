package com.liurui.redis.demo1;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.liurui.redis.demo1.service.UserService;
import com.liurui.redis.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("demo1")
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
     * 当缓存不存在时，调用load会有多次调用；
     * 为了确保只有线程load数据，需要使用注解CachePenetrationProtect
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


    /**
     * 测试下guava的load机制，当不存在数据时，多个线程过来，load是否被调用了多次
     * 结论：
     * 只会被调用一次，其他的等待
     *
     * @throws InterruptedException
     */
    @Test
    public void testGuava() throws InterruptedException {
        final LoadingCache<Integer, User> cache = CacheBuilder.newBuilder()
                .concurrencyLevel(25)
                .build(new CacheLoader<Integer, User>() {
                    @Override
                    public User load(Integer key) throws Exception {
                        System.out.println("compute");
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        User user = new User();

                        user.setId(key);
                        user.setName("ren");
                        return user;
                    }
                });

        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> {
            try {
                System.out.println(cache.get(10));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            try {
                System.out.println(cache.get(10));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            try {
                System.out.println(cache.get(10));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

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
}
