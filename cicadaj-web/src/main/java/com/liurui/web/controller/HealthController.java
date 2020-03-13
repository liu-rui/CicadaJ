package com.liurui.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author : 刘锐
 * @date : 2018/11/9 11:49
 * @description : 健康检查
 */
@RestController
public class HealthController {

    @RequestMapping(path = {"/health/check"}, method = {RequestMethod.HEAD, RequestMethod.GET})
    public void check(HttpServletResponse response) {
        File file = new File("healthcheck.html");

        response.setStatus(file.exists() ? 200 : 404);
    }

}
