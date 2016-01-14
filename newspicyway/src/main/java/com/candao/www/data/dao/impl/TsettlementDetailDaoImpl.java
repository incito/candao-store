package com.candao.www.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.common.page.Page;
import com.candao.www.data.dao.TsettlementDetailMapper;
import com.candao.www.data.model.TsettlementDetail;

@Repository
public class TsettlementDetailDaoImpl implements TsettlementDetailMapper {

	@Autowired
    private DaoSupport dao;
	
 
	@Override
	public TsettlementDetail get(java.lang.String id) {
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
	public int delete(java.lang.String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return dao.delete(PREFIX + ".delete", params);
	}
	
	@Override
	public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize) {
		return dao.page(PREFIX + ".page", params, current, pagesize);
	}

	@Override
	public int insert(TsettlementDetail tsettlement) {
		return dao.insert(PREFIX + ".insert", tsettlement);
	}

	@Override
	public int update(TsettlementDetail tsettlement) {
		return dao.update(PREFIX + ".update", tsettlement);
	}

	@Override
	public int  insertOnce(ArrayList<TsettlementDetail> listInsert){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", listInsert);
		return dao.insert(PREFIX + ".insertOnce", params);
	}
 
	@Override
	public void deleteBySettleId(String settleid){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", settleid);
//		dao.delete(PREFIX + ".insertHistory", params);
		
//		params.put("orderid", settleid);
		dao.delete(PREFIX + ".deleteBySettleId", params);
	}
	
	@Override
	public void calDebitAmount(Map<String, Object> orderDetailMap){
		  dao.get(PREFIX + ".calDebitAmount", orderDetailMap);
	}
}
