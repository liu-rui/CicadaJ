package com.liurui.redis.demo8.service;

import com.liurui.redis.entity.User;
import com.liurui.redis.lock.DistributedLock;
import org.springframework.stereotype.Service;

/**
 * @author liu-rui
 * @date 2019-08-15 10:08
 * @description
 */
@Service
public class UserServiceImpl implements UserService {
    @DistributedLock("#id")
    @Override
    public User add(int id)
    {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(id, "ren");
    }
}
