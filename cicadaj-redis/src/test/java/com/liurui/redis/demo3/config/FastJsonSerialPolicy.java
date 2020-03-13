package com.liurui.redis.demo3.config;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.SerialPolicy;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("fastJson")
public class FastJsonSerialPolicy  implements SerialPolicy {
    @Override
    public Function<Object, byte[]> encoder() {
        return new Function<Object, byte[]>() {
            @Override
            public byte[] apply(Object o) {
                return JSON.toJSONBytes(o);
            }
        };
    }

    @Override
    public Function<byte[], Object> decoder() {
        return new Function<byte[], Object>() {
            @Override
            public Object apply(byte[] bytes) {
                final Object ret = JSON.parse(bytes);

                return ret;
            }
        };
    }
}
