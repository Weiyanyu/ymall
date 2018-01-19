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

    @Scheduled(cron="0 */1 * * * ?")
    public void closeOrderTaskV4() {
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getStringProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            closeOrder(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
        } else {
            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = RedisShardedPoolUtil.getSet(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
                //再次用当前时间戳getset。
                //返回给定的key的旧值，->旧值判断，是否可以获取锁
                //当key没有旧值时，即key不存在时，返回nil ->获取锁
                //这里我们set了一个新的value值，获取旧的值。
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))) {
                    //真正获取到锁
                    closeOrder(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
                } else {
                    log.info("没有获取到分布式锁:{}", Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
                }
            } else {
                log.info("没有获取到分布式锁:{}", Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
            }
        }
        log.info("关闭订单定时任务结束");
    }


    private void closeOrder(String lockName) {
        log.info("获得分布式锁{}", Thread.currentThread().getName());
        RedisShardedPoolUtil.expire(lockName, 5);
        int hour = PropertiesUtil.getIntegerProperty("close.order.task.time.hour",2);
        log.info("执行任务");
        RedisShardedPoolUtil.del(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
    }

}
