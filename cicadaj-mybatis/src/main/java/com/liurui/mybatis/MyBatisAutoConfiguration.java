package com.liurui.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu-rui
 * @date 16:31
 * @description mybatis自动配置类
 * @since 0.1.0
 */
@Configuration
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
public class MyBatisAutoConfiguration {
    /**
     * 分页时，最大的页大小，默认为500,-1为不限制
     */
    @Value("${cicadaj.mybatis.maxPageSize:500}")
    private int limit;

    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        paginationInterceptor.setLimit(limit);
        return paginationInterceptor;
    }

    @ConditionalOnProperty(prefix = "cicadaj.mybatis.sql", name = "enabled")
    @Bean
    public SqlInterceptor sqlInterceptor() {
        return new SqlInterceptor();
    }

    @Bean
    AutoFillMetaObjectHandler autoFillMetaObjectHandler() {
        return new AutoFillMetaObjectHandler();
    }
}
