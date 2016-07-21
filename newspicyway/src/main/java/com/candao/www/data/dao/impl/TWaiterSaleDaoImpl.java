package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TWaiterSaleDao;

@Repository
public class TWaiterSaleDaoImpl implements TWaiterSaleDao{

	@Autowired
	private DaoSupport daoSupport;
	
	/**
	 * 查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> waiterSaleListProcedure(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".waiterSaleListProcedure",params);
	}
	
	/**
	 * 按订单查询服务员销售列表
	 * @author weizhifang
	 * @since 2016-3-15
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getWaiterSaleDetail(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".getWaiterSaleDetail",params);
	}
	
	/**
	 * 查询服务员菜品信息
	 * @author weizhifang
	 * @sice 2016-3-26
	 * @param params
	 * @return
	 */
	public Map<String,Object> getWaiterDishInfo(Map<String,Object> params){
		return daoSupport.get(PREFIX + ".getWaiterDishInfo",params);
	}
}
