package com.example.mybatis.mybatisdemo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author wangrenzhe
 * @date 2020/8/11 16:32
 */
@Service
public class AsyncService {

    @Async
    public void hello() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("数据处理中...");
    }

}
