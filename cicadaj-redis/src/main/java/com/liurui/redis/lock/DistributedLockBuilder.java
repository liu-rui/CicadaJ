package com.liurui.redis.lock;

/**
 * @author liu-rui
 * @date 2019-08-15 14:00
 * @description 分布式锁构造器
 */
public interface DistributedLockBuilder {

    /**
     * 构建一个锁对象,会一直等待的哦
     *
     * @param name 锁名称
     * @return 锁对象 {@link DistributedLockable}
     */
    default DistributedLockable build(String name) {
        return build(name, -1);
    }

    /**
     * 构建一个锁对象
     *
     * @param name     锁名称
     * @param waitTime 获取锁的等待时间，单位为毫秒；当<=0时，永久等待。
     * @return 锁对象 {@link DistributedLockable}
     */
    DistributedLockable build(String name, long waitTime);
}
