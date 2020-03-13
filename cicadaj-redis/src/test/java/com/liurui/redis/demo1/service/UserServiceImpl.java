package com.liurui.redis.demo1.service;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.liurui.redis.entity.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @CreateCache(cacheType = CacheType.LOCAL)
//    @CachePenetrationProtect //当缓存访问未命中的情况下，对并发进行的加载行为进行保护
            Cache<Integer, User> cache;

    @Override
    public User get(int id) {
        return cache.computeIfAbsent(id, (i) -> {
            System.out.println("starting");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            User user = new User();

            user.setId(i);
            user.setName("ren");
            System.out.println("completed");
            return user;
        });
    }


    @Override
    public User getUsingAnnotation(int id) {
        System.out.println("starting");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = new User();

        user.setId(id);
        user.setName("ren");
        System.out.println("completed");
        return user;
    }

    @Override
    public void lock()   {
        try (AutoReleaseLock lock = cache.tryLock(2, 10, TimeUnit.MINUTES)) {
            if (lock != null) {
                System.out.println("getlock  success");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("done");
            }else{
                System.out.println("getlock  failed");
            }
        }
    }
}
