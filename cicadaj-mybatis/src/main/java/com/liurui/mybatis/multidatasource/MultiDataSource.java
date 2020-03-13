package com.liurui.mybatis.multidatasource;

import java.lang.annotation.*;

/**
 * @author liu-rui
 * @date 2019/10/10 下午8:09
 * @description
 * @since 0.3.0
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiDataSource {
    String value();
}
