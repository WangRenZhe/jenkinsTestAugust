package com.example.mybatis.mybatisdemo.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangrenzhe
 * @date 2020/9/2 12:55 组件是容器中的组件，才能使用容器提供的功能
 */
@Configuration
// @ConfigurationProperties(prefix = "person")
@Data
public class Person {
    // 从配置文件中获取值
    @Value("${person.name}")
    private String name;
    // #{SpEL}，这种ConfigurationProperties不支持
    @Value("#{11*2}")
    private Integer age;
    @Value("true")
    private Boolean boss;
    private List<String> haList;
    // 注意：@Value注解不支持复杂类型封装
    private Map<String, String> haMap;
    private Date birth;
}
