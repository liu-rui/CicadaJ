package com.liurui.rabbitmq.web.log;

import com.liurui.rabbitmq.ReturnData;
import com.liurui.rabbitmq.ReturnDataCode;
import com.liurui.rabbitmq.limit.LimitAdvice;
import com.liurui.rabbitmq.limit.LimitException;
import com.liurui.rabbitmq.log.StringAdapter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: 刘锐
 * @date: 18-12-24 下午7:30
 * @description: controller层日志统一抓取类
 */
@Aspect
@Slf4j
public class WebLogAdvice implements Ordered {
    /**
     * 确保在限流拦截器之前执行
     *
     * @see LimitAdvice
     */
    private static final int ORDER = Ordered.LOWEST_PRECEDENCE - 500;

    @Resource
    private WebLogContext webLogContext;

    @Autowired
    WebLogProperties webLogProperties;
    @Autowired
    StringAdapter stringAdapter;


    @Override
    public int getOrder() {
        return ORDER;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *) && !within(com.liurui.rabbitmq.web.controller.HealthController)")
    public Object around(ProceedingJoinPoint point) {
        long begin = System.currentTimeMillis();
        Object ret;
        try {
            ret = point.proceed();
            long escapedMillis = System.currentTimeMillis() - begin;

            if (ret instanceof ReturnData) {
                ReturnData returnData = (ReturnData) ret;

                returnData.setElapsed(escapedMillis);
            }
            long limitMillis = webLogProperties.getSlowQueryLimit().toMillis();

            if (limitMillis > 0 && escapedMillis >= limitMillis) {
                if (log.isWarnEnabled()) {
                    log.warn(getContent(point, ret, escapedMillis));
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info(getContent(point, ret, escapedMillis));
                }
            }
        } catch (Throwable ex) {
            long escapedMillis = System.currentTimeMillis() - begin;
            MethodSignature signature = (MethodSignature) point.getSignature();
            Class returnType = signature.getReturnType();

            if (ex instanceof LimitException) {
                final LimitException limitException = (LimitException) ex;
                final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

                response.setStatus(limitException.getCode());
            }

            if (ex instanceof ReturnDataCode) {
                if (returnType.equals(ReturnData.class)) {
                    ReturnData returnData = ReturnData.error((ReturnDataCode) ex);

                    returnData.setElapsed(escapedMillis);
                    ret = returnData;
                } else {
                    throw new IllegalStateException("返回类型必须是ReturnData,否则的话请在controller层Try下手动捕获异常。", ex);
                }
                if (log.isWarnEnabled()) {
                    log.warn(getContent(point, ret, escapedMillis));
                }
                return ret;
            }

            if (returnType.equals(ReturnData.class)) {
                ReturnData returnData = ReturnData.serverError();

                returnData.setElapsed(escapedMillis);
                ret = returnData;
            } else {
                throw new IllegalStateException("返回类型必须是ReturnData,否则的话请在controller层Try下手动捕获异常。", ex);
            }

            if (log.isErrorEnabled()) {
                log.error(getContent(point, ret, escapedMillis), ex);
            }
        } finally {
            webLogContext.clearData();
        }
        return ret;
    }

    private String getContent(ProceedingJoinPoint point, Object ret, long escapedMillis) {
        StringBuilder sbArgs = new StringBuilder();

        for (Object arg : point.getArgs()) {
            if (sbArgs.length() != 0) {
                sbArgs.append(",");
            }
            sbArgs.append(stringAdapter.to(arg));
        }

        return String.format("%s  共消耗%d毫秒，返回结果为:%s%s",
                String.format("%s.%s [%s]",
                        point.getSignature().getDeclaringType().getSimpleName(),
                        point.getSignature().getName(),
                        sbArgs.toString()),
                escapedMillis,
                stringAdapter.to(ret),
                getAddtionalData());
    }

    private String getAddtionalData() {
        List<Object> dataList = webLogContext.getDataList();

        if (dataList == null || dataList.isEmpty()) {
            return "";
        }
        return String.format("；附加信息为:%s", stringAdapter.to(dataList));
    }
}