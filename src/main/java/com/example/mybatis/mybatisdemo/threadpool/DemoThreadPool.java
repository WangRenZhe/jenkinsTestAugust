package com.example.mybatis.mybatisdemo.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangrenzhe
 * @date 2020/8/6 15:26
 */
@Slf4j
public class DemoThreadPool {
    private static ExecutorService mainThreadPoolExecutor =
        new ThreadPoolExecutor(20, 20, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>(512),
            new BasicThreadFactory.Builder().namingPattern("demo-main-thread-pool-%d").daemon(true).build(),
            (r, executor) -> {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    log.error("demoMainThreadPoolExecutor rejectedExecution fail!", e);
                }
            });

    public static ExecutorService getMainThreadPoolExecutor() {
        return mainThreadPoolExecutor;
    }
}
