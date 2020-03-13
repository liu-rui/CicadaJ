package com.liurui.mybatis.multidatasource;

import com.google.common.base.Strings;
import freemarker.template.utility.ClassUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author liu-rui
 * @date 2019/10/11 下午12:09
 * @description
 * @since 0.3.0
 */
@Log4j2
public class MultiDataSourcePointcut extends StaticMethodMatcherPointcut {
    @Autowired
    MultiDataSourceMapConfig multiDataSourceMapConfig;

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        boolean ret = matchesImpl(method, targetClass);

        if (ret) {
            if (log.isDebugEnabled()) {
                log.debug("匹配到DataSource: method={}, declaringClass={}, targetClass={}",
                        method.getName(),
                        ClassUtil.getShortClassName(method.getDeclaringClass()),
                        targetClass == null ? null : ClassUtil.getShortClassName(targetClass));
            }
        }
        return ret;
    }

    public boolean matchesImpl(Method method, Class<?> targetClass) {
        if (exclude(targetClass.getName())) {
            return false;
        }
        String dataSource = multiDataSourceMapConfig.getDataSource(method, targetClass);

        if (!Strings.isNullOrEmpty(dataSource)) {
            return true;
        }
        MultiDataSource annotation = AnnotationUtils.findAnnotation(method, MultiDataSource.class);

        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(targetClass, MultiDataSource.class);
        }

        if (annotation != null) {

            if (Strings.isNullOrEmpty(annotation.value())) {
                throw new IllegalStateException(String.format("DataSource注解的value值无效，类：%s 方法：%s",
                        ClassUtil.getShortClassName(method.getDeclaringClass()),
                        method.getName()));
            }
            multiDataSourceMapConfig.put(method, targetClass, annotation.value());
        }
        return annotation != null;
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
