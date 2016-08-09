package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TSettlementDetailChildDao;

/**
 * 结算方式明细表子表
 * @author weizhifang
 * @since 2015-11-21
 *
 */
@Repository
public class TSettlementDetailChildDaoImpl implements TSettlementDetailChildDao {

	@Autowired
	private DaoSupport dao;
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> querySettDetailList(Map<String,Object> params){
		return dao.find(PREFIX + ".querySettDetailList",params);
	}
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettList(Map<String,Object> params){
		return dao.get(PREFIX + ".querySettList",params);
	}
	
	/**
	 * 获取结算方式详细名称
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryPayName(Map<String,Object> params){
		return dao.get(PREFIX + ".queryPayName",params);
	}
}
