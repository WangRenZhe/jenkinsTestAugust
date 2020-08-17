package com.example.mybatis.mybatisdemo.controller;

import com.example.mybatis.mybatisdemo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangrenzhe
 * @date 2020/8/11 16:33
 */

@RestController
public class AsyncController {
    @Autowired
    private AsyncService asyncService;

    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        asyncService.hello();
        return "success";
    }

}
