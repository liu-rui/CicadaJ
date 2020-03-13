package com.liurui.rabbitmq;

/**
 * @author liu-rui
 * @date 2019-08-20 15:57
 * @description 失败的消息通过日志文件记录下来
 */
public interface FailedMessageLogByFile {
    void log(String filePath, String content);
}
