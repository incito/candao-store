package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 结算方式明细表子表
 * @author weizhifang
 * @since 2015-11-21
 *
 */
public interface TSettlementDetailChildDao {

	public final static String PREFIX = TSettlementDetailChildDao.class.getName();
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> querySettDetailList(Map<String,Object> params);
	
	/**
	 * 查询结算方式明细
	 * @author weizhifang
	 * @since 2015-11-21
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettList(Map<String,Object> params);
	
	/**查询银行卡名称*/
	public Map<String,Object> queryPayName(Map<String,Object> params);
}
