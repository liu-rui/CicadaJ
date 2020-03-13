package com.liurui.redis.demo7;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.liurui.redis.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@SpringBootApplication
@EnableMethodCache(basePackages = "")
@ActiveProfiles("demo7")
public class AppTests {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() {
        redisTemplate.opsForValue().set("1", new User(1, "ren"));
        final User user = (User) redisTemplate.opsForValue().get("1");

        System.out.println(user);
    }
}
