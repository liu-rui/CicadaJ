package com.liurui.mybatis.multidatasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author liu-rui
 * @date 2019/10/11 下午6:15
 * @description
 * @since 0.3.0
 */
public class MultiRoutingDataSource extends AbstractRoutingDataSource {
    @Autowired
    MultiDataSourceContext multiDataSourceContext;
    @Autowired
    MultiDataSourceProperties multiDataSourceProperties;


    @PostConstruct
    public void init() throws SQLException {
        multiDataSourceProperties.check();

        if (Strings.isNullOrEmpty(multiDataSourceProperties.getMaster())) {
            String firstDataSource = multiDataSourceProperties.getItems().keySet().toArray(new String[0])[0];

            multiDataSourceProperties.setMaster(firstDataSource);
        }
        Map<Object, Object> dataSourceMap = Maps.newHashMapWithExpectedSize(multiDataSourceProperties.getItems().size());

        for (Map.Entry<String, Properties> entry : multiDataSourceProperties.getItems().entrySet()) {
            final Properties properties = entry.getValue();
            DruidDataSource druidDataSource = new DruidDataSource();

            druidDataSource.setDriverClassName(properties.getProperty("driver-class-name"));
            druidDataSource.setUrl(properties.getProperty("url"));
            druidDataSource.setUsername(properties.getProperty("username"));
            druidDataSource.setPassword(properties.getProperty("password"));
            druidDataSource.configFromPropety(properties);
            druidDataSource.init();

            dataSourceMap.put(entry.getKey(), druidDataSource);

            if (multiDataSourceProperties.getMaster().contentEquals(entry.getKey())) {
                this.setDefaultTargetDataSource(druidDataSource);
            }
        }
        this.setTargetDataSources(dataSourceMap);
    }

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return multiDataSourceContext.peek();
    }
}
