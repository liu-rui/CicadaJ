package com.liurui.limit;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

/**
 * @author liu-rui
 * @date 2019/12/5 上午10:17
 * @description
 * @since
 */
public class LimitAdvice implements MethodInterceptor, Ordered {
    private static final int ORDER = Ordered.LOWEST_PRECEDENCE - 400;

    @Autowired
    LimitContext limitContext;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final Limitable limitable = limitContext.get(invocation.getMethod());

        if (!limitable.tryAcquire()) {
            throw new LimitException(limitable.getLimiter().httpStatus(),
                    limitable.getLimiter().message());
        }
        return invocation.proceed();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
