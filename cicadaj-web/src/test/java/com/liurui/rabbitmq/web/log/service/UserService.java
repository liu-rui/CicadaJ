package com.liurui.rabbitmq.web.log.service;


import com.liurui.rabbitmq.ReturnData;

public interface UserService {
    ReturnData<Integer> get(int id);
}
