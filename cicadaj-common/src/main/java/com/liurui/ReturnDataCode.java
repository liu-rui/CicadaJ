package com.liurui;

/**
 * @author: 刘锐
 * @date: 18-12-12 下午7:02
 * @description: 返回数据状态码接口
 * @since 0.1
 */
public interface ReturnDataCode {

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    int getCode();

    /**
     * 获取消息
     *
     * @return 消息
     */
    String getMessage();
}
