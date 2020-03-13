package com.liurui.redis.demo3.service;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.liurui.redis.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @CreateCache(cacheType = CacheType.REMOTE)
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
}
