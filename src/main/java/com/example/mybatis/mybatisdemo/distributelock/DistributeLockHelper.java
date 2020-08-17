package com.example.mybatis.mybatisdemo.distributelock;

import com.alibaba.fastjson.JSON;
import com.example.mybatis.mybatisdemo.check.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author wangrenzhe
 * @date 2020/8/6 00:37
 */
@Component
@Slf4j
public class DistributeLockHelper {
    private static final String LOCKER_PREFIX = "lock_";

    private static final Long DEFAULT_LEASE_TIME = 3000L;

    @Autowired
    private RedissonConnector redissonConnector;

    public <T, R> R lockProcessFunction(String resourceName, Function<T, R> lockInvokeFunction, T t)
        throws InterruptedException {
        return lockProcessFunction(resourceName, lockInvokeFunction, t, DEFAULT_LEASE_TIME);
    }

    public <T, R> R lockProcessFunction(String resourceName, Function<T, R> lockInvokeFunction, T t, long leaseTime)
        throws InterruptedException {
        RedissonClient redissonClient = redissonConnector.getClient();
        RLock lock = redissonClient.getLock(LOCKER_PREFIX + resourceName);

        // Wait for 3 seconds and automatically unlock it after lockTime seconds
        boolean success = lock.tryLock(3000, leaseTime, TimeUnit.MILLISECONDS);
        if (success) {
            try {
                return lockInvokeFunction.apply(t);
            } catch (Exception e) {
                log.error("lockInvokeFunction invoke error param T:{}", JSON.toJSONString(t), e);
                throw new RuntimeException("lockInvokeFunction invoke error");
            } finally {
                lock.unlock();
            }
        }
        throw new RuntimeException("lock error");
    }

    public long lock(String resourceName, long leaseTime) throws InterruptedException {
        if (StringUtils.isBlank(resourceName)) {
            Preconditions.checkNotBlank(resourceName, "resourceName is blank");
        }
        RedissonClient redissonClient = redissonConnector.getClient();
        RLock lock = redissonClient.getLock(LOCKER_PREFIX + resourceName);

        boolean success = lock.tryLock(3000, leaseTime, TimeUnit.MILLISECONDS);
        if (success) {
            return System.currentTimeMillis();
        }
        return 0L;
    }

    public void unlock(String resourceName, long leaseTime, long lockTime) {
        if (lockTime <= 0L) {
            return;
        }
        Preconditions.checkNotBlank(resourceName, "resourceName is blank");
        Preconditions.checkArgument(leaseTime > 0, "leaseTime must greater than zero");
        // 如果已经超时，则不需操作
        long expired = lockTime + leaseTime;
        if (System.currentTimeMillis() > expired) {
            return;
        }
        RedissonClient redissonClient = redissonConnector.getClient();
        RLock lock = redissonClient.getLock(LOCKER_PREFIX + resourceName);
        // 确认锁是否存在，存在则解锁
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

}
