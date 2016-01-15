package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TCoupons;

public interface TCouponsDao {
    
public final static String PREFIX = TCouponsDao.class.getName();
    
	public TCoupons get(java.lang.String couponid);
	
	public <K, V> Map<K, V> findOne(java.lang.String couponid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(TCoupons record);
	
	public int update(TCoupons record);
	
	public int delete(java.lang.String couponid );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	public TCoupons findCouponByDishId(String dishid);
}