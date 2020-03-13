package com.liurui.rabbitmq.limit;

import org.aopalliance.aop.Advice;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liu-rui
 * @date 2019/12/5 上午10:17
 * @description
 * @since 0.5.0
 */
@Aspect
public class LimitAdvisor extends AbstractPointcutAdvisor {
    @Autowired
    LimitPointcut pointcut;

    @Autowired
    LimitAdvice advice;

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }
}
