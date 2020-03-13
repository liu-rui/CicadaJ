package com.liurui.redis.lock;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liu-rui
 * @date 2019-08-15 9:33
 * @description 锁的配置
 */
@Data
@ConfigurationProperties("cicadaj.redis.distributed-lock")
public class DistributedLockProperties {
    /**
     * 获取锁等待时间，单位为毫秒，-1为永久等待；
     */
    public static final long DEFAULT_WAIT_TIME = -1;

    /**
     * 获取锁等待时间，单位为毫秒，-1为永久等待；
     */
    private long waitTime = DEFAULT_WAIT_TIME;
}
