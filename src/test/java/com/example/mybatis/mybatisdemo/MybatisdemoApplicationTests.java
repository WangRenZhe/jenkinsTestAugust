package com.example.mybatis.mybatisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;

@SpringBootTest
class MybatisdemoApplicationTests {
    @Autowired
    private JavaMailSenderImpl mailSender;

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
    }

}
