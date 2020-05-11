package com.alibaba.sca.temp.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class MainController {

    @Value("${test.user.name}")
    private String userName;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello " + userName;
    }
}
