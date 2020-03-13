package com.liurui.rabbitmq;

/**
 * @author liu-rui
 * @date 2019-08-15 16:26
 * @description
 * @since 0.1
 */
public enum ReturnDataCodeEnum implements ReturnDataCode {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),


    /**
     * 服务器端异常状态编码
     */
    SERVER_ERROR(500, "服务端异常"),

    /**
     * 服务器忙，稍后重试哈
     */
    BUSY(584, "服务器忙，稍后重试哈"),

    ;

    private int code;
    private String message;

    ReturnDataCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
