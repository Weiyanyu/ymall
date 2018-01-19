package com.ymall.task;

import com.ymall.common.Const;
import com.ymall.common.RedisShardedPool;
import com.ymall.service.IOrderService;
import com.ymall.util.PropertiesUtil;
import com.ymall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class CloseOrderTask {

    @Autowired
    private IOrderService orderService;

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

    @Scheduled(cron = "0 */1 * * * ?")
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


    private void closeOrder(String lockName) {
        log.info("获得分布式锁{}", Thread.currentThread().getName());
        RedisShardedPoolUtil.expire(lockName, 5);
        int hour = PropertiesUtil.getIntegerProperty("close.order.task.time.hour",2);
        log.info("执行任务");
        RedisShardedPoolUtil.del(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
    }

}
