package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TBusinessDataDetailDao;

import java.util.List;
import java.util.Map;

/**
 * 营业数据明细表
 * @author Administrator
 *
 */
@Repository
public class TBusinessDataDetailDaoImpl implements TBusinessDataDetailDao {

	@Autowired
	private DaoSupport daoSupport;


	public List<Map<String, Object>> isgetBusinessDetail(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".isgetBusinessDetail", params); 
	}
	
	public List<Map<String, Object>> getOrderInfo(Map<String, Object> params) {
		return daoSupport.find(PREFIX + ".getOrderInfo", params); 
	}
}
