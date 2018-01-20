package com.ymall.util;

import com.ymall.common.RedisPool;
import com.ymall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisPoolUtil {
    /**
     * 重新设置过期时间，时间单位是秒
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis;
        Long result;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key {} error ", key, e);
            return null;
        }
        return result;
    }

    /**
     *设置带过期时间的key,时间单位是秒
     */
    public static String setex(String key, String value, int exTime) {
        Jedis jedis;
        String result;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String set(String key, String value) {
        Jedis jedis;
        String result;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String get(String key) {
        Jedis jedis;
        String result;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static Long del(String key) {
        Jedis jedis;
        Long result;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        RedisShardedPoolUtil.set("name", "yeonon");
        String name = RedisShardedPoolUtil.get("name");
        String name1 = RedisShardedPoolUtil.setex("name", "yeonon", 60*2);
        Long time1 = RedisShardedPoolUtil.expire("name", 60*10);
        Long time2 = RedisShardedPoolUtil.del("name");

    }
}
