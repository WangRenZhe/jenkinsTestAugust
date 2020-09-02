package com.example.mybatis.mybatisdemo.controller;

import com.example.mybatis.mybatisdemo.distributelock.DistributeLockHelper;
import com.example.mybatis.mybatisdemo.threadpool.DemoThreadPool;
import com.example.mybatis.mybatisdemo.trycatch.TryCatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author wangrenzhe
 * @date 2020/8/6 15:23
 */
@Controller
@Slf4j
public class DistributedLockController {
    @Autowired
    private DistributeLockHelper distributeLockHelper;

    @RequestMapping(value = "/redlock")
    @ResponseBody
    public String testRedlock() throws Exception {

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);
        ExecutorService mainThreadPoolExecutor = DemoThreadPool.getMainThreadPoolExecutor();
        for (int i = 0; i < 5; ++i) {
            // create and start threads
            mainThreadPoolExecutor.submit(new Worker(startSignal, doneSignal));
        }
        // let all threads proceed 预备，抢！
        startSignal.countDown();
        doneSignal.await();
        return "All processors done. Shutdown connection";
    }

    @RequestMapping(value = "/selfredlock")
    @ResponseBody
    public String selfRedLock() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);
        ExecutorService mainThreadPoolExecutor = DemoThreadPool.getMainThreadPoolExecutor();
        for (int i = 0; i < 5; ++i) {
            // create and start threads
            mainThreadPoolExecutor.submit(new SelfWorker(startSignal, doneSignal));
        }
        // let all threads proceed 预备，抢！
        startSignal.countDown();
        doneSignal.await();
        return "All processors done. Shutdown connection";

    }

    class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                // 所以线程等在这，待会一起开始抢
                startSignal.await();
                distributeLockHelper.lockProcessFunction("test", (String param) -> doTask(doneSignal), null);
            } catch (Exception e) {

            }
        }

    }

    class SelfWorker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        SelfWorker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                // 所以线程等在这，待会一起开始抢
                startSignal.await();
                long lockTime = distributeLockHelper.lock("test", 3000L);
                if (lockTime > 0L) {
                    doTask(doneSignal);
                    distributeLockHelper.unlock("test", 3000L, lockTime);
                } else {
                    log.error("lock error");
                }
                // distributeLockHelper.lockProcessFunction("test", (String param) -> doTask(doneSignal), null);
            } catch (Exception e) {
                log.error("process wrong");
            } finally {

            }
        }

    }

    String doTask(CountDownLatch doneSignal) {
        System.out.println(Thread.currentThread().getName() + " start");
        Random random = new Random();
        int _int = random.nextInt(200);
        System.out.println(Thread.currentThread().getName() + " sleep " + _int + "millis");
        try {
            Thread.sleep(_int);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end");
        doneSignal.countDown();
        return "done";
    }

}
