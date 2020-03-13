package com.liurui.rabbitmq.limit;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author liu-rui
 * @date 2019/12/5 上午10:18
 * @description
 * @since
 */
public class LimitPointcut extends StaticMethodMatcherPointcut {

    @Autowired
    LimitContext limitContext;


    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (exclude(targetClass.getName())) {
            return false;
        }
        Limiter annotation = AnnotationUtils.findAnnotation(method, Limiter.class);

        if (annotation != null) {
            Limitable limitable = new StandaloneLimiter();

            limitable.init(annotation);
            limitContext.put(method, limitable);
            return true;
        }
        return false;
    }

    private boolean exclude(String name) {
        if (name.startsWith("java")) {
            return true;
        }
        if (name.startsWith("org.springframework")) {
            return true;
        }
        if (name.indexOf("$$EnhancerBySpringCGLIB$$") >= 0) {
            return true;
        }
        if (name.indexOf("$$FastClassBySpringCGLIB$$") >= 0) {
            return true;
        }
        return false;
    }
}
