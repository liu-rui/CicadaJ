package com.liurui.rabbitmq.web.log.controller;

import com.liurui.rabbitmq.ReturnData;
import com.liurui.rabbitmq.web.log.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/g")
    public ReturnData<Integer> get(int id) {
        return userService.get(id);
    }
}
