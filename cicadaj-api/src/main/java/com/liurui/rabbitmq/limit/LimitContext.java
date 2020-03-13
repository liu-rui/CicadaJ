package com.liurui.rabbitmq.limit;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author liu-rui
 * @date 2019/12/5 上午11:16
 * @description
 * @since
 */
public class LimitContext {
    private Map<Method, Limitable> limiters = Maps.newHashMap();

    void put(Method method, Limitable limitable) {
        limiters.put(method, limitable);
    }

    Limitable get(Method method) {
        return limiters.get(method);
    }
}
