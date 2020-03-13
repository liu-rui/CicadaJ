package com.liurui.rabbitmq.web.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : 刘锐
 * @date : 2018/11/9 11:49
 * @description : swagger配置属性
 */
@ConfigurationProperties("daling.webapi.swagger")
@Data
public class SwaggerProperies {

    /**
     * swagger文档标题
     */
    private String title = "api查看器";

    /**
     * swagger文档的描述信息
     */
    private String description = "api服务说明";

    /**
     * swagger文档作者
     */
    private String author;

    /**
     * swagger文档联系人的主页
     */
    private String url;

    /**
     * swagger文档联系人的邮箱
     */
    private String email;

    /**
     * swagger文档版本
     */
    private String version;
    /**
     * api接口所在的包
     */
    private String basePackage = "";
}
