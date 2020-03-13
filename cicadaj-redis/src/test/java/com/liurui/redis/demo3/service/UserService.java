package com.liurui.redis.demo3.service;


import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.liurui.redis.entity.User;

public interface UserService {
    User get(int id);

    @Cached(name = "userService", key = "#id",cacheNullValue = true,cacheType = CacheType.REMOTE , expire = 60)
    User getUsingAnnotation(int id);
}