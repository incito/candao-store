package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TCoupons;

public interface TCouponRuleDao {
	
public final static String PREFIX = TCouponRuleDao.class.getName();
    
	public TCoupons get(java.lang.String ruleid);
	
	public <K, V> Map<K, V> findOne(java.lang.String ruleid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(List<TCouponRule> list);
	
	public int update(TCouponRule record);
	/**
	 * 删除优惠的所有规则
	 * @param couponid
	 * @return
	 */
	public int delete(java.lang.String couponid );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

	public List<TCouponRule> findRuleByDishId(String dishId);
}