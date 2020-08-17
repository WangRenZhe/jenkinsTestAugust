package com.example.mybatis.mybatisdemo.distributelock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wangrenzhe
 * @date 2020/8/6 00:12
 */
@Component
public class RedissonConnector {

    RedissonClient redisson;

    @PostConstruct
    public void init() {
        try {
            Config config = new Config();

            config.setCodec(new org.redisson.client.codec.StringCodec());

            // 指定使用单节点部署方式
            config.useSingleServer().setAddress("redis://192.168.0.103:6379");
            config.useSingleServer().setConnectionPoolSize(500);
            config.useSingleServer().setIdleConnectionTimeout(10000);
            // 同任何节点建立连接时的等待超时。时间单位是毫秒。
            config.useSingleServer().setConnectTimeout(30000);
            // 等待节点回复命令的时间。该时间从命令发送成功时开始计时。
            config.useSingleServer().setTimeout(10000);
            config.useSingleServer().setPingConnectionInterval(30000);
            // 当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
            config.useSingleServer().setRetryInterval(3000);

            config.useSingleServer().setConnectionMinimumIdleSize(50);
            redisson = Redisson.create(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RedissonClient getClient() {
        return redisson;
    }

}
