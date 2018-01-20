package com.ymall.util;

import com.ymall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisShardedPoolUtil {
    /**
     * 重新设置过期时间，时间单位是秒
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
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
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String set(String key, String value) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String getSet(String key, String value) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getset key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static Long setnx(String key, String value) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setnx key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static Long del(String key) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();
        RedisPoolUtil.set("name", "yeonon");
        String name = RedisPoolUtil.get("name");
        String name1 = RedisPoolUtil.setex("name", "yeonon", 60*2);
        Long time1 = RedisPoolUtil.expire("name", 60*10);
        Long time2 = RedisPoolUtil.del("name");

    }


}
