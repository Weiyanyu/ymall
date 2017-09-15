package com.ymall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String PREFIX = "token_";

    private static LoadingCache<String, String> localcache =
            CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                //当使用get方法的时候，会默认调用load方法处理数据，这里返回字符串的null防止空指针异常
                //这里cacheLoader不是函数式接口，所以无法使用lambda简化
                public String load(String s) throws Exception {
                    return "null";
                }
            });


    public static void setKey(String key, String val) {
        localcache.put(key, val);
    }

    public static String getValue(String key) {
        String val = null;
        try {
            val = localcache.get(key);
            if ("null".equals(val)) {
                return null;
            }
            return val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
