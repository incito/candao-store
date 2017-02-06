package com.candao.www.preferential.precache;

public class BaseCache {
	/** 缓存的KEY **/
	private String Key;
	/** 缓存数据集 **/
	private Object value;
	/** 更新时间 **/
	private long timeout;
	/** 是否终止 **/
	private boolean expired;

	public BaseCache() {
	}

	public BaseCache(String key, Object value, boolean expired) {
		this.Key = key;
		this.value = value;
		this.expired = expired;
	}

	public BaseCache(String key, Object value, long timeout, boolean expired) {
		this.Key = key;
		this.value = value;
		this.timeout = timeout;
		this.expired = expired;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

}
