package com.liurui.mybatis;

import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

/**
 * @author liu-rui
 * @date 2019/10/25 下午2:48
 * @description
 * @since
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
@Log4j2
public class SqlInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Statement statement;
            Object firstArg = invocation.getArgs()[0];
            if (Proxy.isProxyClass(firstArg.getClass())) {
                statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
            } else {
                statement = (Statement) firstArg;
            }

            if (statement != null && statement instanceof DruidPooledPreparedStatement) {
                final DruidPooledPreparedStatement preparedStatement = (DruidPooledPreparedStatement) statement;
                String sql = preparedStatement.getSql().replaceAll("[\\s]+", StringPool.SPACE).replace("?", "%s");
                MetaObject stmtMetaObj = SystemMetaObject.forObject(preparedStatement);

                try {
                    final Map<String, Object> value = (Map<String, Object>) stmtMetaObj.getValue("stmt.parameters");

                    if (!CollectionUtils.isEmpty(value)) {
                        Object[] objs = new Object[value.size()];

                        for (int i = 0; i < value.size(); i++) {
                            objs[i] = getValue((JdbcParameter) value.get(i));
                        }
                        sql = String.format(sql, objs);
                    }
                } catch (Exception ex) {
                }

                try {
                    final Object[] value = (Object[]) stmtMetaObj.getValue("stmt.binds");

                    if (ArrayUtils.isNotEmpty(value)) {
                        sql = String.format(sql, value);
                    }
                } catch (Exception ex) {
                }
                log.info("sql语句:{}", sql);
            }
        } catch (Exception ex) {
            log.error("打印sql语句时出现了异常", ex);
        }
        return invocation.proceed();
    }


    private String getValue(JdbcParameter jdbcParameter) {
        return jdbcParameter.getValue().toString();
    }

    private String getValue(Object obj) {
        return obj.toString();
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
