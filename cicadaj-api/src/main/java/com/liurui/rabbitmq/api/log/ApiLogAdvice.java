package com.liurui.rabbitmq.api.log;

import com.liurui.rabbitmq.log.StringAdapter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liu-rui
 * @date 2019/11/20 下午5:43
 * @description
 * @since 0.4.0
 */
@Slf4j
public class ApiLogAdvice implements MethodInterceptor {
    @Autowired
    ApiLogProperties apiLogProperties;
    @Autowired
    StringAdapter stringAdapter;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long begin = System.currentTimeMillis();
        Object ret;
        try {
            ret = invocation.proceed();
            long escapedMillis = System.currentTimeMillis() - begin;
            long limitMillis = apiLogProperties.getSlowQueryLimit().toMillis();

            if (limitMillis > 0 && escapedMillis >= limitMillis) {
                if (log.isWarnEnabled()) {
                    log.warn(getContent(invocation, ret, escapedMillis));
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info(getContent(invocation, ret, escapedMillis));
                }
            }
        } catch (Throwable ex) {
            long escapedMillis = System.currentTimeMillis() - begin;

            if (log.isErrorEnabled()) {
                log.error(getErrorMessage(invocation, escapedMillis), ex);
            }
            throw ex;
        }
        return ret;
    }

    private String getContent(MethodInvocation invocation, Object ret, long escapedMillis) {
        return String.format("%s  共消耗%d毫秒，返回结果为:%s",
                getMethodSignature(invocation),
                escapedMillis,
                stringAdapter.to(ret));
    }

    private String getErrorMessage(MethodInvocation invocation, long escapedMillis) {
        return String.format("%s  共消耗%d毫秒；产生了异常,杯具，尽快处理吧!",
                getMethodSignature(invocation),
                escapedMillis);
    }


    private String getMethodSignature(MethodInvocation invocation) {
        StringBuilder sbArgs = new StringBuilder();

        for (Object arg : invocation.getArguments()) {
            if (sbArgs.length() != 0) {
                sbArgs.append(",");
            }
            sbArgs.append(stringAdapter.to(arg));
        }
        return String.format("%s [%s]",
                getShortName(invocation),
                sbArgs.toString());
    }

    private String getShortName(MethodInvocation invocation) {
        return String.format("service.%s.%s",
                invocation.getMethod().getDeclaringClass().getSimpleName(),
                invocation.getMethod().getName());
    }
}
