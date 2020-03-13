package com.liurui.redis.demo5.service;


import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.liurui.redis.entity.User;

public interface UserService {

    @Cached(cacheNullValue = true,cacheType = CacheType.REMOTE)
    User getUsingAnnotation(int id);
}