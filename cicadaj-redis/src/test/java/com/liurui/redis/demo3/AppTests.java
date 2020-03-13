package com.liurui.redis.demo3;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.liurui.redis.demo3.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("demo3")
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

    @Test
    public void testgetUsingAnnotation() {
        System.out.println(userService.getUsingAnnotation(1));
        System.out.println(userService.getUsingAnnotation(1));
        System.out.println(userService.getUsingAnnotation(1));
    }
}
