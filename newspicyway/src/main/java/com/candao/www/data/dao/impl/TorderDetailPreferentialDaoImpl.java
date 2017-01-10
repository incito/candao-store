package com.candao.www.data.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TorderDetailPreferentialDao;
import com.candao.www.data.model.TbOrderDetailPreInfo;
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
	public int addBatchorderPreInfo(List<TbOrderDetailPreInfo> detailPreInfos) {
		return daoSupport.insert(PREFIX + ".addBatchorderPreInfo", detailPreInfos);
	}

	@Override
	public int deleteBachInfo(List<TorderDetailPreferential> detailPreferentials) {
		return 0;
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
	public List<TorderDetailPreferential> getTorderDetailSbyOrderid(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".getTorderDetailSbyOrderid", params);
	}

	@Override
	public int deleteDetilPreFerInfo(Map<String, Object> params) {
		if(params.containsKey("clear")){
			String delFalg=String.valueOf(params.get("clear"));
			if(delFalg.equals("1")){
				params.put("clear","1");
			}
		}else{
			params.put("clear","0");
		}
		
		daoSupport.delete(PREFIX + ".deleteDetilPreFerInfo", params);
		daoSupport.delete(PREFIX + ".deleteDetileSubPreInfo", params);
		return 1;
	}
	@Override
	public int deleteSubPreInfo(Map<String, Object> params) {
		return daoSupport.delete(PREFIX + ".deleteSubPreInfo", params);
	}

	@Override
	public int deleteDetileSubPreInfo(Map<String, Object> params) {
		params.put("clear", String.valueOf(params.get("clear")));
		return daoSupport.delete(PREFIX + ".deleteDetileSubPreInfo", params);
	}

	@Override
	public int deleteForXinladao(Map<String, Object> params) {
		return daoSupport.delete(PREFIX + ".deleteForXinladaoInfo", params);
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

	@Override
	public List<TorderDetailPreferential> getPresentAndspecialPriclist(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".getPresentAndspecialPriclist", params);
	}

	@Override
	public List<TorderDetailPreferential> getAllPreInfolist(Map<String, Object> params) {
		return  daoSupport.find(PREFIX + ".getAllPreInfolist", params);
	}

	

}
