package com.liurui.mybatis.multidatasource;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.aop.Advice;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liu-rui
 * @date 2019/10/10 下午8:12
 * @description
 * @since 0.3.0
 */
@Log4j2
@Aspect
public class MultiDataSourceAdvisor extends AbstractPointcutAdvisor {
    @Autowired
    MultiDataSourcePointcut pointcut;
    @Autowired
    MultiDataSourceInterceptor multiDataSourceInterceptor;

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return multiDataSourceInterceptor;
    }
}
