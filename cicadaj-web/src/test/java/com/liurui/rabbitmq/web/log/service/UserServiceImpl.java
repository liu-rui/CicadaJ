package com.liurui.rabbitmq.web.log.service;

import com.liurui.rabbitmq.ReturnData;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public ReturnData<Integer> get(int id) {
        int a = 10;
        int b = 0;
        int c = a / b;

        return ReturnData.success(c);
    }
}
