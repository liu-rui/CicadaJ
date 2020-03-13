package com.liurui.web.log.controller;

import com.liurui.ReturnData;
import com.liurui.web.log.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/g")
    public ReturnData<Integer> get(int id ) {
        return userService.get(id);
    }
}
