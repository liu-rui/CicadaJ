package com.liurui.mybatis.multidatasource;

import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Properties;

/**
 * @author liu-rui
 * @date 2019/10/11 下午7:47
 * @description
 * @since 0.3.0
 */
@Data
@ConfigurationProperties(prefix = "cicadaj.mybatis.multi-data-source")
public class MultiDataSourceProperties {
    /**
     * 默认使用的数据源名称
     */
    private String master;

    /**
     * 数据源列表
     */
    private Map<String, Properties> items;

    public void check() {
        if (CollectionUtils.isEmpty(items)) {
            throw new IllegalStateException("多数据源配置中数据源列表为空，请配置cicadaj.mybatis.multi-data-source.items");
        }

        if (Strings.isNullOrEmpty(master) && items.size() > 1) {
            throw new IllegalStateException("当多数据源配置中数据源列表为多个时必须配置默认的数据源名称，请配置cicadaj.mybatis.multi-data-source.master");
        }

        if(!Strings.isNullOrEmpty(master) &&  !items.containsKey(master)){
            throw  new IllegalStateException("你配置的默认多数据源名称不在列表中，请检查配置cicadaj.mybatis.multi-data-source.master");
        }
    }
}
