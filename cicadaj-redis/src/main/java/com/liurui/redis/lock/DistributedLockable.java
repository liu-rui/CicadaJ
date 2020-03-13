package com.liurui.redis.lock;

/**
 * @author liu-rui
 * @date 2019-08-15 14:01
 * @description 分布式锁
 */
public interface DistributedLockable extends AutoCloseable {
    boolean lock();
    @Override
    void close();
}
