package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TRethinkSettlementDao;

@Repository
public class TRethinkSettlementDaoImpl implements TRethinkSettlementDao {

	@Autowired
	private DaoSupport daoSupport;
	
	/**
	 * 查询反结算数据
	 * @author weizhifang
	 * @since 2015-11-18
	 * @param branchId
	 * @return
	 */
	public <T, K, V> List<T> queryRethinkSettlementBefore(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".queryRethinkSettlementBefore", params);
	}
	
	/**
	 * 查询反结算后数据
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryRethinkSettlementAfter(String orderId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderid", orderId);
		return daoSupport.get(PREFIX + ".queryRethinkSettlementAfter", params);
	}
	
	/**
	 * 根据用户ID查询用户名
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param userId
	 * @return
	 */
	public String queryUserNameByJobNumber(String userId,String branchId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("branchId", branchId);
		return daoSupport.get(PREFIX + ".queryUserNameByJobNumber", params);
	}
	
	/**
	 * 查询结账单
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryOrder(Map<String,Object> params){
		return daoSupport.get(PREFIX + ".queryOrder", params);
	}
	
	/**
	 * 查询菜品信息
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> queryOrderDetail(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".queryOrderDetail", params);
	}
	
	/**
	 * 查询优惠信息
	 * @author weizhifang
	 * @since 2015-11-20
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> queryPreferenceDetail(Map<String,Object> params){
		return daoSupport.find(PREFIX + ".queryPreferenceDetail", params);
	}
	
	/**
	 * 查询结算信息
	 * @author weizhifang
	 * @since 2015-11-20
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettlementDetail(Map<String,Object> params){
		return daoSupport.get(PREFIX + ".querySettlementDetail", params);
	}
	
	/**
	 * 查询桌号
	 * @author weizhifang
	 * @since 2015-12-9
	 * @param params
	 * @return
	 */
	public String queryTableNo(Map<String,Object> params){
		return daoSupport.getSqlSessionTemplate().selectOne(PREFIX + ".queryTableNo", params);
	}
}
