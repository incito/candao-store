package com.candao.www.dataserver.model;

/**
 * Created by ytq on 2016/4/27.
 */
public class CacheData {
    private String key;
    private Object value;
    private long createTime;
    private int expireTime;

    public CacheData(String key, Object value, int expireTime) {
        this.key = key;
        this.value = value;
        this.createTime = System.currentTimeMillis();
        this.expireTime = expireTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
}
