package com.candao.www.data.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TsettlementMapper;
import com.candao.www.data.model.Tsettlement;
import com.candao.www.webroom.model.SettlementInfo;

@Repository
public class TsettlementDaoImpl implements TsettlementMapper {

	@Autowired
    private DaoSupport dao;
	
	
//	@Override
//	public int insertOnce(List<Tsettlement> tsettlements){
//		return dao.insert(PREFIX + ".insertOnce", tsettlements);
//	}
	
	public int insertOnce(List<Tsettlement> tsettlements){
		return dao.insert(PREFIX + ".insertOnce", tsettlements);
	}
 
	@Override
	public Tsettlement get(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".get", params);
	}
	
	@Override
	public <K, V> Map<K, V> findOne(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.get(PREFIX + ".findOne", params);
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		return dao.find(PREFIX + ".find", params);
	}

	 

	@Override
	public int delete(java.lang.String id,String reason) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", id);
		params.put("reason", reason);
		
		dao.delete(PREFIX + ".insertHistory", params);
		
		dao.delete(PREFIX + ".insertDetailHistory", params);
		 
		params.put("orderid", id);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public int insert(Tsettlement tsettlement) {
		return dao.insert(PREFIX + ".insert", tsettlement);
	}

	@Override
	public int update(Tsettlement tsettlement) {
		return dao.update(PREFIX + ".update", tsettlement);
	}

	@Override
	public int reverseOrder(Tsettlement tsettlement) {
		return dao.update(PREFIX + ".reverseOrder", tsettlement);
		
	}

	@Override
	public Tsettlement getTotalAmount(Date loginTime){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("beginTime", loginTime);
//		Tsettlement settlement = new Tsettlement();
//		settlement.setInserttime(loginTime);
		return dao.get(PREFIX + ".getTotalAmount", params);
	}
 
	@Override
	public  List<Tsettlement> deleteByOrderNo(String orderId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderId);
		return dao.find(PREFIX + ".find", params);
	}

	@Override
	public void insertRebackSettle(SettlementInfo settlementInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertRebackSettleDetail(SettlementInfo settlementInfo) {
		// TODO Auto-generated method stub
		
	}
}
