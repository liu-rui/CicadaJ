package com.liurui.web.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 刘锐
 * @date : 2018/11/9 11:49
 * @description : swagger配置
 */
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperies.class)
@ConditionalOnProperty(name = "cicadaj.web.swagger.enabled", matchIfMissing = true)
@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket creatRestApi(SwaggerProperies swaggerProperies, ApiInfo apiInfo) {
        //指定是否开启swagger
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).enable(true).select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                //指定rest接口所在的位置
                .apis(RequestHandlerSelectors.basePackage(swaggerProperies.getBasePackage()))
                //指定要测试哪些api
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    @Bean
    public ApiInfo apiInfo(SwaggerProperies swaggerProperies) {
        //指定页面的大标题
        return new ApiInfoBuilder().title(swaggerProperies.getTitle())
                //关于这个测试页面的描述
                .description(swaggerProperies.getDescription())
                //指定作者的相关信息
                .contact(new Contact(swaggerProperies.getAuthor(), swaggerProperies.getUrl(), swaggerProperies.getEmail()))
                //指定版本并创建
                .version(swaggerProperies.getVersion()).build();
    }
}
