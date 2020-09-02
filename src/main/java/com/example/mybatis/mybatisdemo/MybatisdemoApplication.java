package com.example.mybatis.mybatisdemo;

import com.alibaba.fastjson.JSON;
import com.example.mybatis.mybatisdemo.dao.DepartmentDOMapper;
import com.example.mybatis.mybatisdemo.model.DepartmentDO;
import com.example.mybatis.mybatisdemo.model.DepartmentDOExample;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
@MapperScan("com.example.mybatis.mybatisdemo.dao")
@ComponentScan("com.example.mybatis")
@EnableCaching
@EnableAsync
@EnableScheduling
public class MybatisdemoApplication implements ApplicationRunner {
    @Autowired
    private DepartmentDOMapper departmentDOMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    /* @Autowired
    private RedisTemplate<Object, Object> departmentDORedisTemplate;*/

    public static void main(String[] args) {
        SpringApplication.run(MybatisdemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // generateArtifacts();
        // insertDepartment();
         selectDepartmentTest();
        //redisTest();
    }

    private void generateArtifacts() throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getResourceAsStream("/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    public void insertDepartment() {
        DepartmentDO departmentDO = new DepartmentDO();
        departmentDO.setDepartmentName("开发");
        DepartmentDO departmentDO1 = new DepartmentDO();
        departmentDO1.setDepartmentName("测试");
        DepartmentDO departmentDO2 = new DepartmentDO();
        departmentDO2.setDepartmentName("市场");
        DepartmentDO departmentDO3 = new DepartmentDO();
        departmentDO3.setDepartmentName("网络");
        DepartmentDO departmentDO4 = new DepartmentDO();
        departmentDO4.setDepartmentName("运营");
        List<DepartmentDO> departmentDOList =
            Lists.newArrayList(departmentDO, departmentDO1, departmentDO2, departmentDO3, departmentDO4);
        departmentDOList.stream().forEach(currentDepartment -> departmentDOMapper.insertSelective(currentDepartment));
    }

    public List<DepartmentDO> selectDepartmentTest() {
        DepartmentDOExample departmentDOExample = new DepartmentDOExample();

        PageHelper.startPage(1, 1);
        List<DepartmentDO> departmentDOList = departmentDOMapper.selectByExample(departmentDOExample);
        System.out.println(JSON.toJSONString(departmentDOList));
        PageHelper.startPage(2, 2);
        List<DepartmentDO> departmentDOList2 = departmentDOMapper.selectByExample(departmentDOExample);
        System.out.println(JSON.toJSONString(departmentDOList2));
        PageHelper.startPage(3, 2);
        List<DepartmentDO> departmentDOList3 = departmentDOMapper.selectByExample(departmentDOExample);
        System.out.println(JSON.toJSONString(departmentDOList3));

        return departmentDOList;
    }

    public void redisTest() {
        /*  stringRedisTemplate.opsForValue().append("msg", "hello");
        String msg = stringRedisTemplate.opsForValue().get("msg");
        System.out.println(msg);
        stringRedisTemplate.opsForList().leftPush("myList", "1");
        stringRedisTemplate.opsForList().leftPush("myList", "2");
        stringRedisTemplate.opsForList().leftPush("myList", "3");
        String myList1 = stringRedisTemplate.opsForList().leftPop("myList");
        String myList2 = stringRedisTemplate.opsForList().rightPop("myList");
        Long myListSize = stringRedisTemplate.opsForList().size("myList");*/

        /* DepartmentDOExample departmentDOExample = new DepartmentDOExample();
        
        PageHelper.startPage(1, 1);
        List<DepartmentDO> departmentDOList = departmentDOMapper.selectByExample(departmentDOExample);
        
        redisTemplate.opsForValue().set("deparment1", departmentDOList.get(0));*/

        // Object deparment1 = redisTemplate.opsForValue().get("deparment1");


        DepartmentDO departmentDO = new DepartmentDO();
        departmentDO.setDepartmentName("ceshi");

        redisTemplate.opsForValue().set("dep-02", departmentDO);
        // System.out.println(JSON.toJSONString(deparment1));
    }

}
