package com.liurui.rabbitmq.log;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liurui.rabbitmq.ReturnData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liu-rui
 * @date 2019-07-24 12:02
 * @description
 */
public class StringAdapterImpl implements StringAdapter, ApplicationContextAware {
    @Autowired
    private LogProperties logProperties;
    private ApplicationContext applicationContext;
    private final List<StringConverter> stringConverters = Lists.newArrayList();

    @PostConstruct
    void init() {
        stringConverters.add(object -> {
            if (Objects.isNull(object)) {
                return ReturnData.success("null");
            }
            return ReturnData.serverError();
        });
        stringConverters.add(object -> {
            if (object instanceof byte[]) {
                return ReturnData.success("byte[]");
            }
            return ReturnData.serverError();
        });
        stringConverters.add(object -> {
            if (object instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) object;
                return ReturnData.success(String.format("MultipartFile[name:%s originalFilename:%s]", file.getName(), file.getOriginalFilename()));
            }
            return ReturnData.serverError();
        });

        Map<String, StringConverter> beans = applicationContext.getBeansOfType(StringConverter.class);

        if (Objects.nonNull(beans) && !beans.isEmpty()) {
            for (Map.Entry<String, StringConverter> entry : beans.entrySet()) {
                stringConverters.add(entry.getValue());
            }
        }
    }

    @Override
    public String to(Object object) {
        try {
            for (StringConverter stringConverter : stringConverters) {
                ReturnData<String> returnData = stringConverter.to(object);

                if (returnData.isSuccess()) {
                    return returnData.getData();
                }
            }
            String ret = JSON.toJSONString(object);

            return Strings.isNullOrEmpty(ret) || ret.length() < logProperties.getObjectMaxLength()
                    ? ret
                    : "太长了，忽略!!";
        } catch (Throwable ex) {
            return object.toString();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
