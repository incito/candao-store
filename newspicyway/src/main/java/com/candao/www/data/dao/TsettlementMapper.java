package com.candao.www.data.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.candao.common.page.Page;
import com.candao.www.data.model.Tsettlement;
import com.candao.www.webroom.model.SettlementInfo;

public interface TsettlementMapper {
	
	   public final static String PREFIX = TsettlementMapper.class.getName();
	   
		public int insertOnce(List<Tsettlement> tsettlements);
	   
		public Tsettlement get(java.lang.String id);
		
		public <K, V> Map<K, V> findOne(java.lang.String id);
		
		public <T, K, V> List<T> find(Map<K, V> params);
		
		public int insert(Tsettlement tsettlement);
		
		public int update(Tsettlement tsettlement);
		
		public int delete(java.lang.String id);
		
		public <E, K, V> Page<E> page(Map<K, V> params, int current, int pagesize);

		public int reverseOrder(Tsettlement tsettlement);

		public Tsettlement getTotalAmount(Date loginTime);

		public List<Tsettlement>  deleteByOrderNo(String orderId);

		public void insertRebackSettle(SettlementInfo settlementInfo);

		public void insertRebackSettleDetail(SettlementInfo settlementInfo);
		
		/**
		 * 根据订单号查询反结次数
		 * @author weizhifang
		 * @since 2016-1-12
		 * @param orderId
		 * @return
		 */
		public String queryAgainSettleNums(String orderId);
		
		/**
		 * 根据订单号修改反结算次数
		 * @author weizhifang
		 * @since 2016-1-12
		 * @param orderId
		 * @param againSettleNums
		 * @return
		 */
		public int updateSettlementHistory(String orderId,int againSettleNums,String authorizerName,String reason);
		
		/**
		 * 往结算表里写数据
		 * @author weizhifang
		 * @since 2016-01-14
		 * @param id
		 * @param reason
		 * @param againSettleNums
		 * @param authorizerName
		 * @return
		 */
		public int insertSettlementHistory(java.lang.String id ,String reason,int againSettleNums,String authorizerName,Double inflated);
		
		/**
		 * 得到会员消费虚增值
		 * @author weizhifang
		 * @since 2016-1-19
		 * @param orderid
		 * @return
		 */
		public Double getMemberInflated(String orderid);
		
		/**
		 * 反结算时修改会员消费虚增值为0
		 * @author weizhifang
		 * @since 2016-1-26
		 * @param orderid
		 * @return
		 */
		public int updateTorderMember(String orderid);
		
		public int selectIsPayWeixin(Map<String, String> dataMap);
		
		public Map<String, Object> fingHistory(Map<String, Object> param);
		
}