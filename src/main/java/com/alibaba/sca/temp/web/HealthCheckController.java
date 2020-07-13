package com.alibaba.sca.temp.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class HealthCheckController {

    @RequestMapping("/health_check")
    @ResponseBody
    public String healthCheck() {
        return "ok";
    }
}
