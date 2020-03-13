package com.liurui.mybatis.multidatasource;

import org.springframework.asm.Type;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liu-rui
 * @date 2019/10/11 下午5:11
 * @description
 * @since 0.3.0
 */
public class MultiDataSourceMapConfig {
    private ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public void put(Method method, Class targetClass, String dataSource) {
        String key = getKey(method, targetClass);

        map.put(key, dataSource);
    }

    public String getDataSource(Method method, Class targetClass) {
        String key = getKey(method, targetClass);

        return map.get(key);
    }

    private static String getKey(Method method, Class targetClass) {
        StringBuilder sb = new StringBuilder();

        sb.append(method.getDeclaringClass().getName());
        sb.append('.');
        sb.append(method.getName());
        sb.append(Type.getMethodDescriptor(method));

        if (targetClass != null) {
            sb.append('_');
            sb.append(targetClass.getName());
        }
        return sb.toString();
    }
}
