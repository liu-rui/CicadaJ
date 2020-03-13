package com.liurui.redis.lock;

import lombok.extern.log4j.Log4j2;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author liu-rui
 * @date 2019-08-15 14:07
 * @description
 */
@Log4j2
class DistributedLockableImpl implements DistributedLockable {
    private RLock rLock;
    private long waitTime;
    boolean isLocked;

    public DistributedLockableImpl(RLock rLock, long waitTime) {
        this.rLock = rLock;
        this.waitTime = waitTime;
    }

    @Override
    public boolean lock() {
        isLocked = lockInternal();
        return isLocked;
    }

    private boolean lockInternal() {
        try {
            if (waitTime <= 0) {
                rLock.lock();
                return true;
            } else {
                return rLock.tryLock(waitTime, TimeUnit.MILLISECONDS);
            }
        } catch (Throwable ex) {
            log.warn("获取锁的时候出现了异常", ex);
            return false;
        }
    }

    @Override
    public void close() {
        if (!isLocked) {
            return;
        }

        try {
            rLock.unlock();
        } catch (Throwable ex) {
            log.warn("释放锁的时候出现了异常", ex);
        }
    }
}
