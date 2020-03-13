package com.liurui.redis.demo5.service;

import com.liurui.redis.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
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
