package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TCouponsDao;
import com.candao.www.data.model.TCoupons;
@Repository
public class TCouponsDaoImpl implements TCouponsDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public TCoupons get(String couponid) {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("couponid", couponid);
		return daoSupport.get(PREFIX+".get", map);
	}

	@Override
	public <K, V> Map<K, V> findOne(String couponid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(TCoupons record) {
		// TODO Auto-generated method stub
		return daoSupport.insert(PREFIX+".insert", record);
	}

	@Override
	public int update(TCoupons record) {
		// TODO Auto-generated method stub
		return daoSupport.update(PREFIX+".update", record);
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
	
	public TCoupons findCouponByDishId(String dishid){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("dishid", dishid);
		return daoSupport.get(PREFIX+".findCouponByDishId", map);
	}

}
