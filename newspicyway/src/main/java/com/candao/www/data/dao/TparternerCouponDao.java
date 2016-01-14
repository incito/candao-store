package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TparternerCoupon;

public interface TparternerCouponDao {
public final static String PREFIX = TparternerCouponDao.class.getName();
    
	public TparternerCoupon get(java.lang.String ruleid);
	
	public <K, V> Map<K, V> findOne(java.lang.String ruleid);
	
	public <T, K, V> List<T> find(Map<K, V> params);
	
	public int insert(List<TparternerCoupon> list);
	
	public int update(TparternerCoupon record);
	/**
	 * 删除优惠的所有规则
	 * @param couponid
	 * @return
	 */
	public int delete(java.lang.String couponid );

	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);
	/**
	 * 合作伙伴  单品优惠的查询    map中传递  菜品id,合作伙伴的id
	 * @param 
	 * @return
	 */
	public List<TparternerCoupon> findRuleByDishId(Map<String, Object> params);

}
