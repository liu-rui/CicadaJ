package com.liurui.redis.demo7;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisTemplateBeanPostProcessor.class)
public class CacheAutoConfiguration {

}
