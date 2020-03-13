package com.liurui.redis.demo1.service;


import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.liurui.redis.entity.User;

public interface UserService {
    User get(int id);

    @Cached(cacheNullValue = true,cacheType = CacheType.LOCAL)
//    @CachePenetrationProtect
    User getUsingAnnotation(int id);

    void lock();
}