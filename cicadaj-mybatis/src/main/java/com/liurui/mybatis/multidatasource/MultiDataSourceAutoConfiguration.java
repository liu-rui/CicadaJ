package com.liurui.mybatis.multidatasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu-rui
 * @date 2019/10/10 下午8:08
 * @description
 * @since 0.3.0
 */
@Configuration
@ConditionalOnProperty(prefix = "cicadaj.mybatis.multi-data-source", name = "enabled", matchIfMissing = false)
@AutoConfigureBefore({DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(MultiDataSourceProperties.class)
public class MultiDataSourceAutoConfiguration {
    @Bean
    public MultiDataSourceContext dataSourceContext() {
        return new MultiDataSourceContext();
    }

    @Bean
    public MultiDataSourceMapConfig dataSourceMapConfig() {
        return new MultiDataSourceMapConfig();
    }

    @Bean
    public MultiDataSourcePointcut dataSourcePointcut() {
        return new MultiDataSourcePointcut();
    }

    @Bean
    public MultiDataSourceInterceptor dataSourceInterceptor() {
        return new MultiDataSourceInterceptor();
    }

    @Bean
    public MultiDataSourceAdvisor dataSourceAdvisor() {
        return new MultiDataSourceAdvisor();
    }

    @Bean
    public javax.sql.DataSource dataSource() {
        return new MultiRoutingDataSource();
    }
}
