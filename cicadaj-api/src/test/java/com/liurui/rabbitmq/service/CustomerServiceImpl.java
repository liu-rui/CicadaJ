package com.liurui.rabbitmq.service;

import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public String get() {
        execute();
        return "hello";
    }

    private void execute() {
        int a = 10;
        int b = 0;
        int c = a / b;
    }
}
