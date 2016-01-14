package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TCouponRuleDao;
import com.candao.www.data.model.TCouponRule;
import com.candao.www.data.model.TCoupons;
@Repository
public class TCouponRuleDaoImpl implements TCouponRuleDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public TCoupons get(String ruleid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <K, V> Map<K, V> findOne(String ruleid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		// TODO Auto-generated method stub
		return daoSupport.find(PREFIX+".find", params);
	}

	@Override
	public int insert(List<TCouponRule> list) {
		// TODO Auto-generated method stub
		return daoSupport.insert(PREFIX+".insert", list);
	}

	@Override
	public int update(TCouponRule record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String couponid) {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("couponid", couponid);
		return daoSupport.delete(PREFIX+".delete", map);
	}

	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		// TODO Auto-generated method stub
		return daoSupport.page(PREFIX+".page", params, current, pagesize);
	}

	@Override
	public List<TCouponRule> findRuleByDishId(String dishId) {
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("dishid", dishId);
		return  daoSupport.find(PREFIX+".findRuleByDishId", map);
	 
	}

}
