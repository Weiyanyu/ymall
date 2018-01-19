package com.ymall.common;

import com.ymall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RedissonManager {
    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    private static final String redis1Ip = PropertiesUtil.getStringProperty("redis1.ip");
    private static final Integer redis1Port = PropertiesUtil.getIntegerProperty("redis1.port",6379);

    private static final String redis2Ip = PropertiesUtil.getStringProperty("redis2.ip");
    private static final Integer redis2Port = PropertiesUtil.getIntegerProperty("redis2.port",6380);

    @PostConstruct
    private void init() {
        try {
            config.useSingleServer().setAddress(redis1Ip + ":" + redis1Port);

            redisson = (Redisson) Redisson.create(config);
            log.info("初始化redisson成功");
        } catch (Exception e) {
            log.info("初始化redisson失败");
            e.printStackTrace();
        }
    }
}
