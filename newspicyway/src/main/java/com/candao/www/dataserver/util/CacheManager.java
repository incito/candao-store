package com.candao.www.dataserver.util;

import com.candao.www.dataserver.model.CacheData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by ytq on 2016/4/27.
 */
public class CacheManager {
    private final static ConcurrentHashMap<String, CacheData> cacheMap = new ConcurrentHashMap<>();

    public static synchronized void put(String key, Object value, int expireTime) {
        CacheData cacheData = new CacheData(key, value, expireTime);
        cacheMap.put(key, cacheData);
    }

    private static synchronized Object get(String key) {
        CacheData cacheData = cacheMap.get(key);
        if (cacheData != null) {
            long nowTime = System.currentTimeMillis();
            if ((nowTime - cacheData.getCreateTime()) / 1000 > cacheData.getExpireTime()) {
                cacheMap.remove(key);
                return null;
            } else {
                return cacheData.getValue();
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CacheManager.put("1", "2", 3);
        System.out.println();
        Thread.sleep(4000L);
        System.out.println(CacheManager.get("1"));
    }

}
