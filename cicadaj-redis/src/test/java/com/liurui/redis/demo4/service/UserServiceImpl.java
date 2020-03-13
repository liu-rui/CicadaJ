package com.liurui.redis.demo4.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedissonClient redissonClient;

    @Override
    public void lock() {
        final RLock ren = redissonClient.getLock("ren");
        try {
            try {
                if(!ren.tryLock(11000 , TimeUnit.MILLISECONDS)){
                    write("lock failed");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                write("lock failed");
                return;
            }
            write(String.format("is locked:%s", ren.isLocked()));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            write("done");
        } finally {
            ren.unlock();
        }
    }

    private void write(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format(" %s %s %s", System.currentTimeMillis() , threadName, message));
    }
}
