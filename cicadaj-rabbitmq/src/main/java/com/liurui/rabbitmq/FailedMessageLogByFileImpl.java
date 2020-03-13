package com.liurui.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author liu-rui
 * @date 2019-08-20 15:58
 * @description
 */
@Slf4j
public class FailedMessageLogByFileImpl implements FailedMessageLogByFile {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void log(String filePath, String content) {
        Assert.notNull(filePath, "filePath 不能为空");
        File file = new File(filePath);
        File parent = file.getParentFile();

        if (Objects.nonNull(parent) && !parent.exists()) {
            //判断存储目录是否存在，不存在则创建
            parent.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.append(String.format("time:%s%n%s%n", DATE_TIME_FORMATTER.format(LocalDateTime.now()), content));
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(String.format("异常消费的消息写入文件异常 内容:%s , 原因:", content), e);
            }
        }
    }
}
