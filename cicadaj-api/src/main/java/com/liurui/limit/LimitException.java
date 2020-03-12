package com.liurui.limit;

import com.liurui.ReturnDataCode;

/**
 * @author liu-rui
 * @date 2019/12/5 上午10:00
 * @description 限流异常
 * @since 0.5.0
 */
public class LimitException extends RuntimeException implements ReturnDataCode {
    private int code;

    public LimitException(int code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
