package com.liurui.redis;

import com.liurui.redis.jetcache.JetCacheConfiguration;
import com.liurui.redis.lock.DistributedLockConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liu-rui
 * @date 2019-08-15 9:54
 * @description
 */
@Configuration
@Import({LettuceConfiguration.class,
        RedissonConfiguration.class,
        JetCacheConfiguration.class,
        DistributedLockConfiguration.class})
public class RedisAutoConfiguraiton {
}
