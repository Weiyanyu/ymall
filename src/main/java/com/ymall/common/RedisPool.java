package com.ymall.common;

import com.ymall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool pool;
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.max.total",20);
    private static Integer maxIdle = PropertiesUtil.getIntegerProperty("redis.max.idle",10);
    private static Integer minIdle = PropertiesUtil.getIntegerProperty("redis.min.idle",0);
    private static Boolean testOnBorrow = PropertiesUtil.getBooleanProperty("redis.testOnBorrow",true);
    private static Boolean testOnReturn = PropertiesUtil.getBooleanProperty("redis.testOnReturn", false);

    private static final String redisIp = PropertiesUtil.getStringProperty("redis.ip");
    private static final Integer redisPort = PropertiesUtil.getIntegerProperty("redis.port",6379);

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000*2);
    }

    static {
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void returnBrokenResouce(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("name","weiyanyu");
        System.out.println(jedis.get("name"));
        returnResource(jedis);
        System.out.println("ok");

        pool.destroy();
    }
}
