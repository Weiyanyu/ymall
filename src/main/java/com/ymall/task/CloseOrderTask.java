package com.ymall.task;

import com.ymall.common.Const;
import com.ymall.common.RedisShardedPool;
import com.ymall.common.RedissonManager;
import com.ymall.service.IOrderService;
import com.ymall.util.PropertiesUtil;
import com.ymall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CloseOrderTask {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedissonManager redissonManager;

    @PreDestroy
    private void delLock() {
        RedisShardedPoolUtil.del(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("定时任务开启");
        //orderService.closeOrder(PropertiesUtil.getIntegerProperty("close.order.task.time.hour",2));
        log.info("定时任务结束");
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2() {
        log.info("定时任务开启");
        long lockTimeOut = Long.parseLong(PropertiesUtil.getStringProperty("lock.timeout","5000"));
        Long result = RedisShardedPoolUtil.setnx(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        if (result != null && result.intValue() == 1) {
            closeOrder(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
        } else {
            log.info("获取锁失败{}", Thread.currentThread().getName());
        }

        log.info("定时任务结束");
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3() {
        log.info("定时任务开启");
        Long lockTimeOut = Long.parseLong(PropertiesUtil.getStringProperty("lock.timeout","5000"));
        Long result = RedisShardedPoolUtil.setnx(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        if (result != null && result.intValue() == 1) {
            log.info("获取锁成功{}",Thread.currentThread().getName());
            closeOrder(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
        } else {
            String lockValueStr = RedisShardedPoolUtil.get(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = RedisShardedPoolUtil.getSet(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
                if (getSetResult == null || StringUtils.equals(lockValueStr, getSetResult)) {
                    closeOrder(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
                } else {
                    log.info("获取锁失败{}", Thread.currentThread().getName());
                }
            } else {
                log.info("获取锁失败{}", Thread.currentThread().getName());
            }

        }

        log.info("定时任务结束");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderV4() {
        RLock rLock = redissonManager.getRedisson().getLock(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
        boolean getLock = false;
        try {
            if (getLock = rLock.tryLock(0, 50, TimeUnit.SECONDS)) {
                log.info("获得分布式锁");
                int hour = PropertiesUtil.getIntegerProperty("close.order.task.time.hour",2);
                //这里放一下循环测试一下，CPU太快了，导致同时两个进程，甚至多个进程获得锁
                for (long i = 0; i < 1000000; i++) {
                    for (int j = 1; j < 1000; j++);
                }
            } else {
                log.info("没有获得分布式锁");
            }

        } catch (InterruptedException e) {
            log.error("获取分布式锁有异常", e);
        } finally {
            if (!getLock) {
                return;
            }
            rLock.unlock();
            log.info("分布式锁释放");
        }
    }


    private void closeOrder(String lockName) {
        log.info("获得分布式锁{}", Thread.currentThread().getName());
        RedisShardedPoolUtil.expire(lockName, 5);
        int hour = PropertiesUtil.getIntegerProperty("close.order.task.time.hour",2);
        log.info("执行任务");
        RedisShardedPoolUtil.del(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
    }

}
