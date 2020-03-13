package com.liurui.redis.demo2.service;


import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.liurui.redis.entity.User;

public interface UserService {
    User get(int id);

    @Cached(cacheNullValue = true,cacheType = CacheType.REMOTE)
    User getUsingAnnotation(int id);

    @Cached(cacheNullValue = true,cacheType = CacheType.BOTH , localExpire = 5,  expire = 10)
    User getUsingAnnotationBoth(int id);

    void lock();
}