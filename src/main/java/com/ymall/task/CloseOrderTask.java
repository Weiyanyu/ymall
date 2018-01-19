package com.ymall.task;

import com.ymall.common.Const;
import com.ymall.common.RedisShardedPool;
import com.ymall.service.IOrderService;
import com.ymall.util.PropertiesUtil;
import com.ymall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloseOrderTask {

    @Autowired
    private IOrderService orderService;

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("定时任务开启");
        //orderService.closeOrder(PropertiesUtil.getIntegerProperty("close.order.task.time.hour",2));
        log.info("定时任务结束");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2() {
        log.info("定时任务开启");
        long lockTimeOut = Long.parseLong(PropertiesUtil.getStringProperty("lock.timeout","5000"));
        Long result = RedisShardedPoolUtil.setnx(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        int hour = PropertiesUtil.getIntegerProperty("close.order.task.time.hour",2);
        if (result != null && result == 1) {
            closeOrder(hour);
        } else {
            log.info("获取锁失败{}", Thread.currentThread().getName());
        }

        log.info("定时任务结束");
    }

    private void closeOrder(int hour) {
        log.info("获得分布式锁{}", Thread.currentThread().getName());
        RedisShardedPoolUtil.expire(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK, 50000);
        log.info("执行任务");
        RedisShardedPoolUtil.del(Const.RedisLock.REDIS_CLOSE_ORDER_LOCK);
    }

}
