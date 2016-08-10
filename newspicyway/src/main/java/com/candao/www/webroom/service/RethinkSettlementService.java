package com.candao.www.webroom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RethinkSettlementService {

	/**
	 * 查询反结算数据
	 * @author weizhifang
	 * @since 2015-11-18
	 * @param branchId
	 * @return
	 */
	public List<Map<String,Object>> queryRethinkSettlement(Map<String,Object> params);
	
	/**
	 * 查询桌号
	 * @author weizhifang
	 * @since 2015-12-9
	 * @param params
	 * @return
	 */
	public String queryTableNo(Map<String,Object> params);
	
	/**
	 * 查询会员消费虚增值
	 * @author weizhifang
	 * @since 2016-01-25
	 * @param orderid
	 * @return
	 */
	public BigDecimal queryMemberInflate(String orderid);
	
	/**
	 * 查询结账单
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public Map<String,Object> statementInfo(Map<String,Object> params);
	
	/**
	 * 根据用户ID查询用户名
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param userId
	 * @return
	 */
	public String queryUserNameByJobNumber(String userId,String branchId);
	
	/**
	 * 查询反结算后数据
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryRethinkSettlementAfter(String orderId);
	
	/**
	 * 查询零头处理方式
	 * @author weizhifang
	 * @since 2016-7-6
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryLingtou(Map<String,Object> params);
}
