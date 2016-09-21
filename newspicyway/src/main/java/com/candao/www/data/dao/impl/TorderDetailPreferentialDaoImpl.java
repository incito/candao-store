package com.candao.www.data.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TorderDetailPreferential;

/**
 * 
 * @author 梁东 订单使用优惠对应关系
 */
@Repository
public class TorderDetailPreferentialDaoImpl implements TorderDetailPreferentialDao {

	@Autowired
	private DaoSupport daoSupport;

	@Override
	public int addBatchInfo(List<TorderDetailPreferential> detailPreferentials) {
		return daoSupport.insert(PREFIX + ".addBatchInfo", detailPreferentials);
	}

	@Override
	public int deleteBachInfo(List<TorderDetailPreferential> detailPreferentials) {
		return 0;
	}

	@Override
	public int addDetailPreFerInfo(TorderDetailPreferential detailPreferential) {
		return daoSupport.insert(PREFIX + ".addDetailPreFerInfo", detailPreferential);
	}

	@Override
	public List<TorderDetailPreferential> queryDetailPreBy(String orderid) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderid", orderid);
		return daoSupport.find(PREFIX + ".getTorderDetailPreS", params);
	}
	@Override
	public List<TorderDetailPreferential> queryDetailPreByGift(String orderId) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderid", orderId);
		return daoSupport.find(PREFIX + ".queryDetailPreByGift", params);
	}

	@Override
	public List<TorderDetailPreferential>  getTorderDetailSbyOrderid(Map<String, Object> params) {
		return  daoSupport.find(PREFIX + ".getTorderDetailSbyOrderid",params);
	}

	@Override
	public int deleteDetilPreFerInfo(Map<String, Object> params) {
		params.put("clear", String.valueOf(params.get("clear")));
		return daoSupport.delete(PREFIX + ".deleteDetilPreFerInfo",params);
	}

	@Override
	public BigDecimal statisticALLDiscount(String orderid) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderid", orderid);
		return daoSupport.get(PREFIX + ".statisticALLDiscount", params);
	}

	@Override
	public <T, K, V> List<T> queryGiveprefer(String orderid) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderid", orderid);
		return daoSupport.find(PREFIX + ".queryGiveprefer", params);
	}



}
