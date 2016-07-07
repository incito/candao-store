package com.candao.www.data.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 反结算报表
 * @author Administrator
 *
 */
public interface TRethinkSettlementDao {

	public final static String PREFIX = TRethinkSettlementDao.class.getName();
	
	/**
	 * 查询反结算数据
	 * @author weizhifang
	 * @since 2015-11-18
	 * @param branchId
	 * @return
	 */
	public <T, K, V> List<T> queryRethinkSettlementBefore(Map<String,Object> params);
	
	/**
	 * 查询反结算后数据
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryRethinkSettlementAfter(String orderId);
	
	/**
	 * 根据用户ID查询用户名
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param userId
	 * @return
	 */
	public String queryUserNameByJobNumber(String userId,String branchId);
	
	/**
	 * 查询结账单
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryOrder(Map<String,Object> params);
	
	/**
	 * 查询菜品信息
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> queryOrderDetail(Map<String,Object> params);
	
	/**
	 * 查询优惠信息
	 * @author weizhifang
	 * @since 2015-11-20
	 * @param params
	 * @return
	 */
	public <T, K, V> List<T> queryPreferenceDetail(Map<String,Object> params);
	
	/**
	 * 查询结算信息
	 * @author weizhifang
	 * @since 2015-11-20
	 * @param params
	 * @return
	 */
	public Map<String,Object> querySettlementDetail(Map<String,Object> params);
	
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
	 * 查询四舍五入和抹零
	 * @author weizhifang
	 * @since 2016-5-30
	 * @param orderid
	 * @return
	 */
	public List<Map<String,Object>> queryMoLing(String orderid);
	
	/**
	 * 查询应收
	 * @author weizhifang
	 * @since 2016-5-30
	 * @param orderid
	 * @return
	 */
	public BigDecimal totalconsumption(String orderid);
	
	/**
	 * 查询实收
	 * @author weizhifang
	 * @since 2016-5-30
	 * @param orderid
	 * @return
	 */
	public BigDecimal paidamount(String orderid);
	
	/**
	 * 查询赠菜金额
	 * @author weizhifang
	 * @since 2016-5-30
	 * @param orderid
	 * @return
	 */
	public BigDecimal giveamount(String orderid);
	
	/**
	 * 获取套餐金额
	 * @author weizhifang
	 * @since 2016-5-31
	 * @param orderid
	 * @return
	 */
	public BigDecimal taocanAmount(String orderid);
	
}
