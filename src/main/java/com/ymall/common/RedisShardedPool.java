package com.ymall.common;

import com.google.common.collect.Lists;
import com.ymall.util.PropertiesUtil;
import org.apache.commons.lang3.ArrayUtils;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;

import java.util.Arrays;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.max.total",20);
    private static Integer maxIdle = PropertiesUtil.getIntegerProperty("redis.max.idle",10);
    private static Integer minIdle = PropertiesUtil.getIntegerProperty("redis.min.idle",0);
    private static Boolean testOnBorrow = PropertiesUtil.getBooleanProperty("redis.testOnBorrow",true);
    private static Boolean testOnReturn = PropertiesUtil.getBooleanProperty("redis.testOnReturn", false);

    private static final String redis1Ip = PropertiesUtil.getStringProperty("redis1.ip");
    private static final Integer redis1Port = PropertiesUtil.getIntegerProperty("redis1.port",6379);

    private static final String redis2Ip = PropertiesUtil.getStringProperty("redis2.ip");
    private static final Integer redis2Port = PropertiesUtil.getIntegerProperty("redis2.port",6380);

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 2 * 1000);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 2 * 1000);

        List<JedisShardInfo> jedisShardInfoList = Lists.newArrayList();
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, ShardedJedis.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    public static void returnBrokenResouce(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();

        for (int i = 0; i < 10; i++) {
            jedis.set("key" + i, "value" + i);
        }

        returnResource(jedis);
        System.out.println("ok");

        pool.destroy();
    }
}
