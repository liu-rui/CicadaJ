package com.liurui.rabbitmq.api.log;

import org.aopalliance.aop.Advice;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

/**
 * @author liu-rui
 * @date 2019/11/20 下午5:42
 * @description
 * @since 0.4.0
 */
@Aspect
public class ApiLogAdvisor extends AbstractPointcutAdvisor implements Ordered {
    private static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;
    @Autowired
    ApiLogPointcut apiLogPointcut;
    @Autowired
    ApiLogAdvice apiLogAdvice;


    @Override
    public Pointcut getPointcut() {
        return apiLogPointcut;
    }

    @Override
    public Advice getAdvice() {
        return apiLogAdvice;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
