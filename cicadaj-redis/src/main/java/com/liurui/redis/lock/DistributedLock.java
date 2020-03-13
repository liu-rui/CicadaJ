package com.liurui.redis.lock;

import java.lang.annotation.*;

/**
 * @author liu-rui
 * @date 2019-08-15 9:30
 * @description 分布式锁
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 名称，支持spel
     *
     * @return
     */
    String value();


    /**
     * 名称前缀，如果为空，使用目标方法的签名作为前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 获取锁的等待时间
     *
     * @return 锁的等待时间，如果为 Long.MIN_VALUE，使用全局配置设置的等待时间, {@link DistributedLockProperties}
     */
    long waitTime() default Long.MIN_VALUE;
}
