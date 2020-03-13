package com.liurui.redis.lock;

import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author liu-rui
 * @date 2019-08-15 9:32
 * @description
 */
@Aspect
@Log4j2
public class DistributedLockAdvice implements Ordered {
    private static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;
    @Autowired
    DistributedLockProperties distributedLockProperties;
    @Autowired
    DistributedLockBuilder distributedLockBuilder;

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Pointcut("@annotation(com.liurui.redis.lock.DistributedLock)")
    public void exec() {
    }

    @Around("exec()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final DistributedLock annotation = AnnotationUtils.findAnnotation(method, DistributedLock.class);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new MethodBasedEvaluationContext(joinPoint.getTarget(),
                method,
                joinPoint.getArgs(),
                new DefaultParameterNameDiscoverer());
        String lockName = parser.parseExpression(annotation.value()).getValue(context, String.class);

        if (Strings.isNullOrEmpty(lockName)) {
            throw new IllegalStateException("获取锁名称失败，不能为空！");
        }
        //如果前缀没有设置的话，使用方法所在地类作为缓存的前缀
        lockName = (Strings.isNullOrEmpty(annotation.prefix())
                ? method.getDeclaringClass().getName() + ":lock:"
                : annotation.prefix())
                + lockName;

        long waitTime = annotation.waitTime() != Long.MIN_VALUE
                ? annotation.waitTime()
                : distributedLockProperties.getWaitTime();
        try (final DistributedLockable distributedLockable = distributedLockBuilder.build(lockName, waitTime)) {
            if (!distributedLockable.lock()) {
                throw new DistributedLockFailedException();
            }
            return joinPoint.proceed();
        }
    }
}
