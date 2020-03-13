package com.liurui.mybatis.multidatasource;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liu-rui
 * @date 2019/10/11 上午9:56
 * @description
 * @since 0.3.0
 */
@Log4j2

public class MultiDataSourceInterceptor implements MethodInterceptor {
    @Autowired
    MultiDataSourceMapConfig multiDataSourceMapConfig;
    @Autowired
    MultiDataSourceContext multiDataSourceContext;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String dataSource = multiDataSourceMapConfig.getDataSource(invocation.getMethod(),
                    invocation.getThis().getClass());

            if (log.isDebugEnabled()) {
                log.debug("method:{}, datasource:{}", invocation.getMethod().getName(), dataSource);
            }
            multiDataSourceContext.push(dataSource);
            return invocation.proceed();
        } finally {
            multiDataSourceContext.pop();
        }
    }
}
