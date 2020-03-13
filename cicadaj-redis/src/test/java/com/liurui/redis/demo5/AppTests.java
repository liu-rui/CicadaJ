package com.liurui.redis.demo5;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@ActiveProfiles("demo5")
public class AppTests {
//    @Autowired
//     UserService userService;


    @Autowired
    StringRedisTemplate  redisTemplate;
    @Autowired
    ConfigurableApplicationContext  applicationContext;
//    @Autowired
//    LettuceConnectionFactory  lettuceConnectionFactory;

    @Test
    public void test() {
        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
//        redisTemplate.opsForValue().set("a" , "b");



//        System.out.println(redisTemplate);
//        System.out.println(lettuceConnectionFactory);

//        System.out.println(userService.getUsingAnnotation(1));
    }
}
