package com.liurui.redis.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "")
@Configuration
public class JetCacheConfiguration {
}
