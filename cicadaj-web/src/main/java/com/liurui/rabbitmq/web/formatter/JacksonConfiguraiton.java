package com.liurui.rabbitmq.web.formatter;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author liu-rui
 * @date 2019-07-12 16:54
 * @description
 */
@Configuration
public class JacksonConfiguraiton {
    @Bean
    public Module module() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(LocalDateTimeConverter.DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(LocalDateTimeConverter.DATE_TIME_FORMATTER));
        return javaTimeModule;
    }

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder(ApplicationContext applicationContext) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        builder.applicationContext(applicationContext);
        builder.dateFormat(DateConverter.DATE_FORMAT.get());
        ArrayList<Module> modules = Lists.newArrayList(new Jdk8Module());

        modules.addAll(applicationContext.getBeansOfType(Module.class).values());
        builder.modules(modules.toArray(new Module[0]));
        return builder;
    }
}
