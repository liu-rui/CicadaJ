package com.liurui.log;


import com.liurui.ReturnData;

/**
 * @author liu-rui
 * @date 2019-07-24 12:01
 * @description
 */
public interface StringConverter {
    ReturnData<String> to(Object object);
}
