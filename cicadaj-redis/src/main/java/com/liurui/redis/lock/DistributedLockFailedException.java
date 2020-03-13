package com.liurui.redis.lock;


import com.liurui.rabbitmq.ReturnDataCode;
import com.liurui.rabbitmq.ReturnDataCodeEnum;

/**
 * @author liu-rui
 * @date 2019-08-15 11:29
 * @description
 */
public class DistributedLockFailedException extends RuntimeException implements ReturnDataCode {
    public DistributedLockFailedException() {
        super(ReturnDataCodeEnum.BUSY.getMessage());
    }

    @Override
    public int getCode() {
        return ReturnDataCodeEnum.BUSY.getCode();
    }
}
