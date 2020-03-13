package com.liurui.rabbitmq.api.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * @author liu-rui
 * @date 2019/11/20 下午5:43
 * @description
 * @since 0.4.0
 */
@Slf4j
public class ApiLogPointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        boolean ret = matchesImpl(method, targetClass);

        if (ret) {
            if (log.isDebugEnabled()) {
                log.debug("匹配到Log: method={}, declaringClass={}, targetClass={}",
                        method.getName(),
                        ClassUtils.getShortName(method.getDeclaringClass()),
                        targetClass == null ? null : ClassUtils.getShortName(targetClass));
            }
        }
        return ret;
    }


    public boolean matchesImpl(Method method, Class<?> targetClass) {
        if (exclude(targetClass.getName())) {
            return false;
        }

        return AnnotationUtils.findAnnotation(targetClass, FeignClient.class) != null ||
                AnnotationUtils.findAnnotation(targetClass, ApiLog.class) != null ||
                AnnotationUtils.findAnnotation(method, ApiLog.class) != null;
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
