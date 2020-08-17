package com.example.mybatis.mybatisdemo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author wangrenzhe
 * @date 2020/8/11 16:58
 */
@Service
public class ScheduledService {
    /**
     * second, minute, hour, day of month, month, and day of week 0 * * * * MON-FRI
     */
    // @Scheduled(cron = "0 * * * * MON-FRI")
    // @Scheduled(cron = "0,1,2,3,4 * * * * MON-FRI")
    //0秒启动每四秒执行一次
    @Scheduled(cron = "0/4 * * * * MON-FRI")

    public void hello() {
        System.out.println("hello...scheduled");
    }

}
