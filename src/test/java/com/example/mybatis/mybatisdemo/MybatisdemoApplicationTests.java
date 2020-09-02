package com.example.mybatis.mybatisdemo;

import com.example.mybatis.mybatisdemo.bean.Person;
import com.example.mybatis.mybatisdemo.threadpool.DemoThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

@Slf4j
@SpringBootTest
class MybatisdemoApplicationTests {
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private Person person;

    @Test
    public void testPerson() {
        log.info("info-info");
        log.warn("warn-warn");
        log.error("error-error");
        System.out.println(person);

    }

    @Test
    public void send() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("通知-今晚开会");
        mimeMessageHelper.setText("<b style='color:red'>今天 晚上开完会吃饭</b>", true);
        mimeMessageHelper.setTo("abcxxx@163.com");
        mimeMessageHelper.setFrom("1234567@qq.com");
        mimeMessageHelper.addAttachment("californian-desert-3840x2160-4k-5k-wallpaper-8k-road-usa-sunset-5718.jpg",
            new File(
                "/Users/xxxx/Downloads/壁纸/californian-desert-3840x2160-4k-5k-wallpaper-8k-road-usa-sunset-5718.jpg"));
        mailSender.send(mimeMessage);
        new HashSet<>();
    }

    @Test
    public void byteBufferTest() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("abcde".getBytes());
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        buffer.flip();
        System.out.println("---------------after flip");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
    }

    @Test
    public void streamChannelWithByteBufferTest() throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileInputStreamChannel = null;
        FileChannel fileOutputStreamChannel = null;
        try {
            fileInputStream = new FileInputStream("/Users/vincentwang/Desktop/截屏2020-08-03 下午4.02.10.PNG");
            fileOutputStream = new FileOutputStream("/Users/vincentwang/Desktop/2.jpg");
            fileInputStreamChannel = fileInputStream.getChannel();
            fileOutputStreamChannel = fileOutputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (fileInputStreamChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                fileOutputStreamChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {

        } finally {
            if (fileOutputStreamChannel != null) {
                fileOutputStreamChannel.close();
            }
            if (fileInputStreamChannel != null) {
                fileInputStreamChannel.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    @Test
    public void channelWithByteBufferTest() throws IOException {
        FileChannel readInputChannel = null;
        FileChannel writeOutPutChannel = null;
        try {
            readInputChannel = FileChannel.open(Paths.get("/Users/vincentwang/Desktop/截屏2020-08-10 上午12.31.08.PNG"));
            writeOutPutChannel = FileChannel.open(Paths.get("/Users/vincentwang/Desktop/3.jpg"),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (readInputChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                writeOutPutChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (readInputChannel != null) {
                readInputChannel.close();
            }
            if (writeOutPutChannel != null) {
                writeOutPutChannel.close();
            }
        }
    }

    @Test
    public void channelWithoutByteBufferTest() throws IOException {
        FileChannel readInputChannel = null;
        FileChannel writeOutPutChannel = null;
        try {
            readInputChannel = FileChannel.open(Paths.get("/Users/vincentwang/Desktop/截屏2020-08-10 上午12.31.08.PNG"));
            writeOutPutChannel = FileChannel.open(Paths.get("/Users/vincentwang/Desktop/3.jpg"),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            // readInputChannel.transferTo(0, readInputChannel.size(), writeOutPutChannel);
            writeOutPutChannel.transferFrom(readInputChannel, 0, readInputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (readInputChannel != null) {
                readInputChannel.close();
            }
            if (writeOutPutChannel != null) {
                writeOutPutChannel.close();
            }
        }
    }

    @Test
    public void testLinkHashMap() {

        LinkedHashMap accessOrderedMap = new LinkedHashMap(16, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) { // 实现自定义删除策略，否则行为就和普遍Map没有区别
                return size() > 3;
            }
        };
        accessOrderedMap.put("Project1", "Valhalla");
        accessOrderedMap.put("Project2", "Panama");
        accessOrderedMap.put("Project3", "Loom");
        accessOrderedMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        }); // 模拟访问
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project3");
        System.out.println("Iterate over should be not affected:");
        accessOrderedMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        // 触发删除
        accessOrderedMap.put("Project4", "Mission Control");
        System.out.println("Oldest entry should be removed:");
        accessOrderedMap.forEach((k, v) -> {// 遍历顺序不变
            System.out.println(k + ":" + v);
        });

    }

    @Test
    public void arrayBlockingQueue() throws InterruptedException {

        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(3);

        DemoThreadPool.getMainThreadPoolExecutor().submit(() -> {
            try {
                arrayBlockingQueue.put("a");
                arrayBlockingQueue.put("b");
                arrayBlockingQueue.put("c");
                arrayBlockingQueue.put("d");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        DemoThreadPool.getMainThreadPoolExecutor().submit(() -> {
            try {
                arrayBlockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

    @Test
    public void reentrantLockConditionTest() {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                shareResource.process(shareResource.reentrantLock, shareResource.conditionB, shareResource.conditionA,
                    1, "这个时候执行conditionA.wait()", "执行场景A", 2);
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                shareResource.process(shareResource.reentrantLock, shareResource.conditionC, shareResource.conditionB,
                    2, "这个时候执行conditionB.wait()", "执行场景B", 3);
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                shareResource.process(shareResource.reentrantLock, shareResource.conditionA, shareResource.conditionC,
                    3, "这个时候执行conditionC.wait()", "执行场景C", 1);
            }
        }).start();
    }

    class ShareResource {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition conditionA = reentrantLock.newCondition();
        Condition conditionB = reentrantLock.newCondition();
        Condition conditionC = reentrantLock.newCondition();
        private Integer number = 2;

        public void process(ReentrantLock reentrantLock, Condition signalCondition, Condition currentCondition, int i,
            String s, String 执行场景, int i2) {
            reentrantLock.lock();
            System.out.println("加锁成功");
            try {
                while (number != i) {
                    System.out.println(s);
                    currentCondition.await();
                }
                System.out.println(执行场景);
                number = i2;
                signalCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("这个时候执行释放锁");
                reentrantLock.unlock();
            }
        }

    }

}
