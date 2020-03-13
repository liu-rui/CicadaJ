package com.liurui.redis.lock;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * @author liu-rui
 * @date 2019-08-15 14:04
 * @description
 */
public class DistributedLockBuilderImpl implements DistributedLockBuilder {
    @Autowired
    RedissonClient redissonClient;

    @Override
    public DistributedLockable build(String name, long waitTime) {
        Assert.hasLength(name, "name must have data");

        return new DistributedLockableImpl(redissonClient.getLock("DL:C:Lock:" + name), waitTime);
    }
}
